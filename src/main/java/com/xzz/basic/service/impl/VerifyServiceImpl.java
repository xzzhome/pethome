package com.xzz.basic.service.impl;

import com.xzz.basic.service.IVerifyService;
import com.xzz.basic.util.StrUtils;
import com.xzz.basic.util.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerifyServiceImpl implements IVerifyService {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public String getImgCode(String key) {
        String code = StrUtils.getComplexRandomString(4);
        redisTemplate.opsForValue().set( key , code , 3, TimeUnit.MINUTES );
        return VerifyCodeUtils.verifyCode(110, 40, code);
    }
}
