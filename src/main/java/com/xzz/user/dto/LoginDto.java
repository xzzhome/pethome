package com.xzz.user.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    //登录类型：0管理员,1用户
    private Integer loginType;
}