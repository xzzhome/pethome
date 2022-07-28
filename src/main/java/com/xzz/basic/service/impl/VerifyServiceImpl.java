package com.xzz.basic.service.impl;

import com.xzz.basic.Constant.VerifyCodeConstants;
import com.xzz.basic.dto.SmsCodeDto;
import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.service.IVerifyService;
import com.xzz.basic.util.SmsUtils;
import com.xzz.basic.util.StrUtils;
import com.xzz.basic.util.VerifyCodeUtils;
import com.xzz.user.domain.User;
import com.xzz.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class VerifyServiceImpl implements IVerifyService {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getImgCode(String key) {
        String code = StrUtils.getComplexRandomString(4);
        redisTemplate.opsForValue().set( key , code , 3, TimeUnit.MINUTES );
        return VerifyCodeUtils.verifyCode(110, 40, code);
    }

    @Override
    public void sendSmsCode(SmsCodeDto smsCodeDto) {
        //1.校验 - 空值校验
        String imageCode = smsCodeDto.getImageCode();
        String imageCodeKey = smsCodeDto.getImageCodeKey();
        String phone = smsCodeDto.getPhone();
        if(StringUtils.isEmpty(phone)){
            throw new BusinessException("手机号不可为空");
        }
        if(StringUtils.isEmpty(imageCodeKey)){
            throw new BusinessException("请刷新页面重新获取图片验证码");
        }
        if(StringUtils.isEmpty(imageCode)){
            throw new BusinessException("图片验证码不能为空");
        }
        //2.校验 - 图片验证码校验
        //2.1.图片验证码是否过期
        Object imageCodeValue = redisTemplate.opsForValue().get(imageCodeKey);
        if(imageCodeValue == null ){
            throw new BusinessException("图片验证码过期，请重新输入");
        }
        //2.2.图片验证码是否正确
        if(!imageCodeValue.toString().equalsIgnoreCase(imageCode)){
            throw new BusinessException("错误的图片验证码，请重新输入");
        }
        //3.校验 - 手机号是否被注册
        User user = userMapper.loadByPhone(phone);
        if(user != null){
            throw new BusinessException("该手机已经注册，请直接登录!");
        }
        //redis存值格式：key[register:18708146200] : value[code:time]
        Object codeObj = redisTemplate.opsForValue().get(VerifyCodeConstants.REGISTER_CODE_PREFIX + phone);
        String code = null;
        if(codeObj != null){//没有过期
            String time = codeObj.toString().split(":")[1];
            long intervalTime = System.currentTimeMillis() - Long.valueOf(time);
            if(intervalTime <= 1*60*1000){ //没过重发时间一分钟
                throw new BusinessException("请勿重复发送!");
            }else{//过了1分钟
                code = codeObj.toString().split(":")[0];
            }
        }else{//没有验证码
            code = StrUtils.getRandomString(4);
        }
        //将验证码数据添加到redis
        redisTemplate.opsForValue().set(VerifyCodeConstants.REGISTER_CODE_PREFIX + phone, code + ":" + System.currentTimeMillis(),3, TimeUnit.MINUTES);//存3分钟
        //发短信
        //SmsUtils.sendSms(phone, "你的验证码为：" + code + "，请在3分钟之内使用!" );  //有次数
        System.out.println("你的验证码为：" + code + "，请在3分钟之内使用!");//可以这样测试
    }
}
