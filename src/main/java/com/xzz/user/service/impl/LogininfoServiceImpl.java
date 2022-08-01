package com.xzz.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xzz.basic.Constant.VerifyCodeConstants;
import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.basic.util.HttpUtil;
import com.xzz.basic.util.JsonResult;
import com.xzz.basic.util.Md5Utils;
import com.xzz.basic.util.StrUtils;
import com.xzz.user.constant.WxConstants;
import com.xzz.user.domain.Logininfo;
import com.xzz.user.domain.User;
import com.xzz.user.domain.Wxuser;
import com.xzz.user.dto.BinderDto;
import com.xzz.user.dto.LoginDto;
import com.xzz.user.mapper.LogininfoMapper;
import com.xzz.user.mapper.UserMapper;
import com.xzz.user.mapper.WxUserMapper;
import com.xzz.user.service.ILogininfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LogininfoServiceImpl extends BaseServiceImpl<Logininfo> implements ILogininfoService {

    @Autowired
    private LogininfoMapper logininfoMapper;
    @Autowired
    private WxUserMapper wxUserMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public JsonResult accountLogin(LoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        Integer loginType = loginDto.getLoginType();
        //1.校验
        //1.1.空值校验
        if(StringUtils.isEmpty(username)
                ||StringUtils.isEmpty(password)
                ||StringUtils.isEmpty(loginType)){
            throw new BusinessException("请输入合法信息!!!");
        }
        //通过用户名与类型判断，数据库中是否存在校验
        Logininfo logininfo = logininfoMapper.loadByAccount(loginDto);
        if(logininfo == null){
            throw new BusinessException("用户名或密码错误!!!");
        }
        //1.2.密码【加密加盐】校验
        String dbMd5Pwd = logininfo.getPassword();//数据库的密码
        String md5Pwd = Md5Utils.encrypByMd5(password + logininfo.getSalt());//前端输入的密码
        if(!dbMd5Pwd.equals(md5Pwd)){
            throw new BusinessException("用户名或密码错误!!!");
        }
        //1.3.状态校验 - 启用，禁用
        if(!logininfo.getDisable()){
            throw new BusinessException("你的账号被冻结，请联系管理员!!!");
        }

        //2.将token和登录信息对象 保存在 redis 30分钟有效
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token,logininfo,30, TimeUnit.MINUTES);

        //3.使用Map将token和登录对象绑定到JsonResult的resultObj中响应给前端
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        logininfo.setSalt(null);
        logininfo.setPassword(null);
        map.put("logininfo",logininfo);

        return JsonResult.me().setResultObj(map);
    }

    @Override
    public JsonResult wechatLogin(String code) {
        //1.根据code发送请求获取token和openid
        String jsonStr = HttpUtil.httpGet( WxConstants.GET_ACK_URL
                            .replace("APPID",WxConstants.APPID)
                            .replace("SECRET",WxConstants.SECRET)
                            .replace("CODE",code));
        //2.将微信平台响应的json字符串 转成 json对象，微信平台响应了token和openid
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        String access_token = jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");

        //3.通过openid去t_wxuser中查询
        Wxuser wxuser = wxUserMapper.loadByOpenid(openid);
        //如果数据库有数据，则证明绑定了
        if(wxuser!=null && wxuser.getUser_id()!=null){
            //4.有 绑定过 - 直接免密登录【获取logininfo对象 登录】
            Logininfo logininfo = logininfoMapper.loadLogininfoByUserId(wxuser.getUser_id());
            String token = UUID.randomUUID().toString();
            //去掉密码和盐值 - 安全
            logininfo.setPassword(null);
            logininfo.setSalt(null);
            redisTemplate.opsForValue().set(token,logininfo,30, TimeUnit.MINUTES);
            Map<String, Object> map = new HashMap<>();
            map.put("token",token);
            map.put("logininfo",logininfo);
            return JsonResult.me().setResultObj(map);

        }else{
            //5.没有 - 需要跳转页面绑定 - 响应给前端进行跳转
            Map<String, Object> map = new HashMap<>();
            map.put("access_token",access_token);
            map.put("openid",openid);
            return JsonResult.me().setMsg("绑定").setResultObj(map);
        }
    }

    @Override
    public JsonResult wechatBinder(BinderDto binderDto) {
        String phone = binderDto.getPhone();
        String verifyCode = binderDto.getVerifyCode();
        String accesstoken = binderDto.getAccessToken();
        String openId = binderDto.getOpenId();
        //校验
        //1.校验 - 空置校验
        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(verifyCode)){
            throw new BusinessException("数据不能为空");
        }
        //2.校验 - 验证码是否过期  //code:1215215155
        Object redisCode = redisTemplate.opsForValue().get(VerifyCodeConstants.REGISTER_CODE_PREFIX + phone);
        if(redisCode==null){
            throw new BusinessException("验证码过期");
        }
        //3.校验 - 验证码是否正确
        if(!redisCode.toString().split(":")[0].equalsIgnoreCase(verifyCode.trim())){
            throw new BusinessException("验证码错误");
        }
        //4.获取WxUser - 发送第三个请求 - json字符串
        String url = WxConstants.GET_USER_URL
                .replace("ACCESS_TOKEN",accesstoken)
                .replace("OPENID",openId);
        String jsonStr = HttpUtil.httpGet(url);
        Wxuser wxuser = jsonStr2Wxuser(jsonStr);//绑定用户：t_wxUser【user_id】 绑定 t_user
        //5.通过phone查询User
        User user = userMapper.loadByPhone(phone);
        //5.1.如果没有 - 则进行注册操作
        if(user == null){
            user = phone2User(phone);
            Logininfo logininfoTmp= user2Logininfo(user);

            logininfoMapper.save(logininfoTmp);//先更新表t-logininfo
            user.setLogininfo_id(logininfoTmp.getId());
            userMapper.save(user);//再更新表t-user
        }
        //6.将User对象的id设置到WxUser对象中
        wxuser.setUser_id(user.getId());
        //7.将WxUser对象添加到数据微
        wxUserMapper.save(wxuser);//最后更新表t-wxuser

        //8.做免密登录
        Logininfo logininfo = logininfoMapper.loadById(user.getLogininfo_id());
        String token = UUID.randomUUID().toString();
        //去掉密码和盐值 - 安全
        logininfo.setPassword(null);
        logininfo.setSalt(null);
        redisTemplate.opsForValue().set(token,logininfo,30, TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("logininfo",logininfo);

        return JsonResult.me().setResultObj(map);
    }

    private Logininfo user2Logininfo(User user) {
        Logininfo logininfo = new Logininfo();
        BeanUtils.copyProperties(user,logininfo);
        logininfo.setType(1);
        return logininfo;
    }

    private User phone2User(String phone) {
        User user = new User();
        user.setUsername(phone);
        user.setPhone(phone);
        String salt = StrUtils.getComplexRandomString(32);
        //随机的纯数据密码
        String randomPwd = StrUtils.getRandomString(6);
        String md5Pwd = Md5Utils.encrypByMd5(randomPwd + salt);
        user.setSalt(salt);
        user.setPassword(md5Pwd);
        return user;
    }

    private Wxuser jsonStr2Wxuser(String jsonStr) {
        Wxuser wxuser = new Wxuser();
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        wxuser.setOpenid(jsonObject.getString("openid"));
        wxuser.setNickname(jsonObject.getString("nickname"));
        wxuser.setSex(jsonObject.getInteger("sex"));
        wxuser.setAddress(jsonObject.getString("country") + jsonObject.getString("province") + jsonObject.getString("city"));
        wxuser.setHeadimgurl(jsonObject.getString("headimgurl"));
        wxuser.setUnionid(jsonObject.getString("unionid"));
        return wxuser;
    }
}
