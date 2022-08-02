package com.xzz.basic.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})//注解能作用在方法上、类上
//Java中的反射：在运行时，动态获取类的各种信息的一种能力
@Retention(RetentionPolicy.RUNTIME)//可以通过反射读取注解
@Inherited//可以被继承
@Documented//可以被javadoc工具提取成文档，可以不加
public @interface PreAuthorize {
    //对应t_permission表中的sn
    String value();
	//对应t_permission表中的name
    String name();
}