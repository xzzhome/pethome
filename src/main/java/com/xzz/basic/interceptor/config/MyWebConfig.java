package com.xzz.basic.interceptor.config;

import com.xzz.basic.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)

            //拦截所有请求
            .addPathPatterns("/**")
            //放行跟登录的请求
            .excludePathPatterns("/login/**")
            //放行跟注册有关的
            .excludePathPatterns("/user/register/**","/verifyCode/**")
            .excludePathPatterns("/shop/settlement","/fastDfs/**")
            //放行swagger
            .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
            //如果写了邮箱激活还要放行邮箱
    }
}