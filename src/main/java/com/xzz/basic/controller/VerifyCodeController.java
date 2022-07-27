package com.xzz.basic.controller;

import com.xzz.basic.service.IVerifyService;
import com.xzz.basic.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}