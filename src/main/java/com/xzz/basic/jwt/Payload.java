package com.xzz.basic.jwt;//载荷数据：
import java.util.Date;

public class Payload<T> {

    private String id;  // jwt的id(token) - 参考JwtUtils
    private T loginData;  // 用户信息：用户数据，不确定，可以是任意类型
    private Date expiration;  // 过期时间 - 参考JwtUtils

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getLoginData() {
        return loginData;
    }

    public void setLoginData(T loginData) {
        this.loginData = loginData;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "id='" + id + '\'' +
                ", loginData=" + loginData +
                ", expiration=" + expiration +
                '}';
    }
}