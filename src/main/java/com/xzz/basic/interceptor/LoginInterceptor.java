package com.xzz.basic.interceptor;

import com.xzz.basic.annotation.PreAuthorize;
import com.xzz.org.mapper.EmployeeMapper;
import com.xzz.user.domain.Logininfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取token
        String token = request.getHeader("token");
        //3.如果有token，通过token获取redis的登录信息
        if(token != null){
            Object obj = redisTemplate.opsForValue().get(token); //Logininfo对象
            if(obj != null){//登录成功，而且没有过期
                //5.如果登录信息不为null - 放行 + 刷新过期时间[重新添加到redis]
                redisTemplate.opsForValue().set(token , obj ,30, TimeUnit.MINUTES);
                //校验权限
                //a.登录成功权限1 - 如果是前台用户 - 直接放行【前台用户只需要登录成功即可】
                Logininfo logininfo = (Logininfo) obj;
                if(logininfo.getType().intValue() == 1){
                    //1.如果是前端用户 user ，不需要校验任何权限，直接放行即可
                    return true;//放行
                }
                //b.登录成功校验2 - 如果是后台用户 - 继续校验权限
                //b1.哪些请求需要校验权限 （答：打了自定义注解PreAuthorize权限的方法）
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                //b2.获取当前请求/接口/controller中方法上的权限信息 - 是否有自定义注解@PreAuthorize
                PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
                //b3.如果preAuthorize为空：该方法中上没有加这个注解 - 说明公共资源不需要校验权限 - 放行
                if(preAuthorize == null){
                    //b31. 没有就放行,公共资源不需要校验权限
                    return true;
                }else{//该方法中上加了这个注解 - 说明特殊资源需要校验权限
                    //b32.有就校验是否有权限访问 - 获取注解上的value值
                    String sn = preAuthorize.value();
                    //b33.查询出当前登录人所拥有的权限：t_permission表中的sn
                    List<String> ownPermissions = employeeMapper.loadPerssionSnByLogininfoId(logininfo.getId());
                    //b34.如果集合中包含当前sn - 说明有权限访问 - 放行
                    if(ownPermissions.contains(sn)){
                        return true;//终于有权限访问了
                    }
                    //b35.如果不包含：告诉他没有权限，请他去联系管理员
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.print("{\"success\":false,\"message\":\"noPermission\"}");
                    writer.close();
                    return false;//阻止放行
                }
            }
        }
        //2.判断token，如果为null - 直接拦截 响应前端 - 跳转到登录页面
        //4.如果登录信息为null - 过期了 直接拦截 响应前端 - 跳转到登录页面
        //告诉浏览器我要给你响应一个json数据，编码集为utf-8
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"success\":false,\"msg\":\"noLogin\"}");
        return false;//不放行
    }
}