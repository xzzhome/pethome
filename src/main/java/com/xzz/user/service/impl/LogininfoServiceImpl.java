package com.xzz.user.service.impl;

import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.basic.util.JsonResult;
import com.xzz.basic.util.Md5Utils;
import com.xzz.user.domain.Logininfo;
import com.xzz.user.dto.LoginDto;
import com.xzz.user.mapper.LogininfoMapper;
import com.xzz.user.service.ILogininfoService;
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

}
