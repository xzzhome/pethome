package com.xzz.user.dto;

import lombok.Data;

@Data
public class UserDto {
    //电话
    private String phone;
    //手机验证码
    private String phoneCode;
    //密码
    private String password;
    //确认密码
    private String confirmPassword;

}