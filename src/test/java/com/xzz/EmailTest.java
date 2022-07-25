package com.xzz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest(classes = PetHomeApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EmailTest {
    
   @Autowired
   private JavaMailSender javaMailSender;

   @Test
    public void send1(){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        //设置发送人
        mailMessage.setFrom("1091526909@qq.com");
        //邮件主题
        mailMessage.setSubject("新型冠状病毒防护指南");
        //邮件内容：普通文件无法解析html标签
        mailMessage.setText("<h1>好好在家待着.....</h1>");
        //收件人
        mailMessage.setTo("1091526909@qq.com");
        //发送邮件
        javaMailSender.send(mailMessage);
    }

    @Test
    public void send2() throws MessagingException {
        //创建复杂邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //发送复杂邮件的工具类
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"utf-8");
        helper.setFrom("1091526909@qq.com");//设置发送人和yml配置文件相同
        helper.setSubject("店铺激活邮件");//邮件主题
        //邮件内容
        helper.setText("<h1>你的店铺已经注册!!!</h1>" +
                "<img src='http://123.207.27.208/group1/M00/00/E9/CgAIC2LdBDCAcrbnABzb1t_QKds204.jpg'>" +
                "<a href='http://localhost:8080/shop/active/22'>点击该链接激活</a>",true);
        //添加附件
        helper.addAttachment("p74.png",new File("D:\\01_班级资料\\03_images\\项目二宠物图片\\p74.png"));
        //收件人
        helper.setTo("798337572@qq.com");
        javaMailSender.send(mimeMessage);//发送邮件
    }
}