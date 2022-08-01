package com.xzz.user.constant;

//微信登录相关常量
public class WxConstants {
    //第一次请求，获取code，授权码
    public static final String APPID = "wxd853562a0548a7d0";
    public static final String SECRET = "4a5d5615f93f24bdba2ba8534642dbb6";
    //第二次请求，获取openID和token，微信唯一凭证id和令牌
    public static final String GET_ACK_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //第三次请求，获取wxuser，微信用户信息
    public static final String GET_USER_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
}