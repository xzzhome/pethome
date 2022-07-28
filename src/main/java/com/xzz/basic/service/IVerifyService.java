package com.xzz.basic.service;

import com.xzz.basic.dto.SmsCodeDto;

public interface IVerifyService {
    String getImgCode(String key);

    void sendSmsCode(SmsCodeDto smsCodeDto);
}
