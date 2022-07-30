package com.xzz.basic.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取token
        String token = request.getHeader("token");
        //3.如果有token，通过token获取redis的登录信息
        if(token != null){
            Object logininfo = redisTemplate.opsForValue().get(token); //Logininfo对象
            if(logininfo != null){//登录成功，而且没有过期
                //5.如果登录信息不为null - 放行 + 刷新过期时间[重新添加到redis]
                redisTemplate.opsForValue().set(token , logininfo ,30, TimeUnit.MINUTES);
                return true;
            }
        }
        //2.判断token，如果为null - 直接拦截 响应前端 - 跳转到登录页面
        //4.如果登录信息为null - 过期了 直接拦截 响应前端 - 跳转到登录页面
        //告诉浏览器我要给你响应一个json数据，编码集为utf-8
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"success\":false,\"msg\":\"noLogin\"}");
        return false;
    }
}