package com.xzz.basic.controller;

import com.xzz.basic.dto.SmsCodeDto;
import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.service.IVerifyService;
import com.xzz.basic.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 验证码接口
 * 1.图形验证码接口
 * 2.手机验证码接口
 */
@RestController
@RequestMapping("/verifyCode")
public class VerifyCodeController {

    @Autowired
    private IVerifyService verifyService;

    @RequestMapping("/image/{key}")
    public JsonResult imageVerifyCode(@PathVariable("key") String key){
        try {
            String value = verifyService.getImgCode(key);
            return JsonResult.me().setResultObj(value);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("系统异常,请稍后重试!");
        }
    }

    /**
     * 获取手机验证码接口
     * @param smsCodeDto
     * @return
     */
    @PostMapping("/smsCode")
    public JsonResult sendSmsCode(@RequestBody SmsCodeDto smsCodeDto){
        try {
            verifyService.sendSmsCode(smsCodeDto);
            return new JsonResult();
        } catch (BusinessException e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("发送失败!"+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("系统异常,请稍后重试!");
        }
    }
    /**
     * 获取绑定微信手机验证码
     * @return
     */
    @PostMapping("/binderSmsCode")
    public JsonResult binderSmsCode(@RequestBody SmsCodeDto smsDto){
        try {
            verifyService.binderSmsCode(smsDto);
            return new JsonResult();
        } catch (BusinessException e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("发送失败，" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("系统繁忙!!!");
        }
    }
}