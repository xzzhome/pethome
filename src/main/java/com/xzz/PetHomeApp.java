package com.xzz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//master修改代码，自己同步
@SpringBootApplication
@MapperScan("com.xzz.*.mapper")//扫描多模块的mapper接口
public class PetHomeApp {
    public static void main(String[] args){
        SpringApplication.run(PetHomeApp.class,args);
    }
}
