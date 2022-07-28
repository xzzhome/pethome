package com.xzz.basic.dto;

import lombok.Data;
@Data
public class SmsCodeDto {
    //手机号码
    private String phone;
    //图形验证码
    private String imageCode;
    //图形验证码的key
    private String imageCodeKey;
}