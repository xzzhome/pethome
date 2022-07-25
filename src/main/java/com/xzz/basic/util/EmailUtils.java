package com.xzz.basic.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/**
 * @author Administrator
 * @description: 发送邮件工具类
 * @date 2022/7/25 16:58
 */
@Component
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;
    /**
     * 发送邮件
     *         from 发送邮箱
     * @param to 接收邮箱
     * @param subject 发送主题
     * @param message 发送内容
     * @param attachment 附件
     * @throws MessagingException
     */
    public void send(String to, String subject, String message, Map<String,File> attachment) {

        try {
            //创建复杂邮件对象
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            //发送复杂邮件的工具类
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"utf-8");
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(message,true);
            //添加附件
            if(attachment!= null) {
                for (Map.Entry<String, File> entry : attachment.entrySet()) {
                    helper.addAttachment(entry.getKey(), entry.getValue());
                }
            }
            //收件人
            helper.setTo(to);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
