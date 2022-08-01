package com.xzz.user.dto;

import lombok.Data;

@Data
public class BinderDto {
    private String phone;
    private String verifyCode;
    private String accessToken;
    private String openId;
}
