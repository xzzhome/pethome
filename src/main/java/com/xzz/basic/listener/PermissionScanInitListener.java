package com.xzz.basic.listener;

import com.xzz.basic.service.IPermissionScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener//申明自定义的web监听器，被容器注册和使用
@Slf4j
public class PermissionScanInitListener implements ServletContextListener {

    @Autowired
    private IPermissionScanService permissionScanService;
    
    //spring容器初始化结束之后被调用
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //这里面的业务随着接口的变多，可能执行时间会非常久，影响性能。影响主线程的启动
        new Thread(new Runnable() {//不用主线程去执行，用一个新的线程去执行
            @Override
            public void run() {
                //可以在这里扫描我们自定义的注解@PreAuthorize，然后将信息存储到t_permission表
                //这样就无需手动录入信息到权限t_permission表了
                log.info("权限初始化开始******************************************");
                System.out.println("权限初始化开始******************************************");
                permissionScanService.scanPermission();
                System.out.println("权限初始化结束******************************************");
            }
        }).start();
    }
 
    //容器销毁的时候执行
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}