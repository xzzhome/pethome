package com.xzz.basic.util;

import com.xzz.basic.jwt.JwtUtils;
import com.xzz.basic.jwt.Payload;
import com.xzz.basic.jwt.RsaUtils;
import com.xzz.user.domain.LoginData;
import com.xzz.user.domain.Logininfo;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;

public class LoginContext {

    public static Logininfo getLogininfo(HttpServletRequest request) {
        try {
            //1.获取token
            String token = request.getHeader("token");
            //2.如果有token【jwtToken】执行里面的代码，没有就去登录
            if (token != null) {
                //3.获取公钥
                PublicKey publicKey = RsaUtils.getPublicKey(RsaUtils.class.getClassLoader().getResource("auth_rsa.pub").getFile());
                try {
                    //4.将jwtToken解析成想要的数据：LoginData
                    Payload<LoginData> payload = JwtUtils.getInfoFromToken(token, publicKey, LoginData.class);
                    //刷新过期时间不做 - 可以用redis保存token串
                    if (payload != null) {//登录成功，而且没有过期
                        //5.如获取登录信息对象
                        Logininfo logininfo = payload.getLoginData().getLogininfo();
                        return logininfo;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
