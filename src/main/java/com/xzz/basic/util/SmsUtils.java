package com.xzz.basic.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 短信发送工具类
 */
public class SmsUtils {

    //用户名
    public static final String UID = "xzzhome2";
    //秘钥
    public static final String KEY = "42FAC45273BF3F2318D2D0C6F73FCE80";

    /**
     * 发送短信
     * @param phones 手机们 a,b
     * @param content 发送内容
     * @return 返回值
     */
    public static String  sendSms(String phones,String content){
        PostMethod post = null;
        try {
            HttpClient client = new HttpClient();
            post = new PostMethod("http://utf8.api.smschinese.cn");
            post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf8");//在头文件中设置转码
            NameValuePair[] data ={ new NameValuePair("Uid", SmsUtils.UID),
                    new NameValuePair("Key", SmsUtils.KEY),
                    new NameValuePair("smsMob",phones),
                    new NameValuePair("smsText",content)};
            post.setRequestBody(data);

            client.executeMethod(post);
            int statusCode = post.getStatusCode();
            System.out.println("statusCode:"+statusCode); //200 404 400
            String result = new String(post.getResponseBodyAsString().getBytes("utf8"));
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return null;
    }

}