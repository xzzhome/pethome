package com.xzz.org.service.impl;


import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.basic.util.BaiduAuditUtils;
import com.xzz.org.domain.Employee;
import com.xzz.org.domain.Shop;
import com.xzz.org.domain.ShopAuditLog;
import com.xzz.org.dto.ShopDto;
import com.xzz.org.mapper.EmployeeMapper;
import com.xzz.org.mapper.ShopAuditLogMapper;
import com.xzz.org.mapper.ShopMapper;
import com.xzz.org.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;


@Service
public class ShopServiceImpl extends BaseServiceImpl<Shop> implements IShopService {

    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private ShopAuditLogMapper shopAuditLogMapper;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void settlement(Shop shop) {
        //一：检验
        //1.1.空值校验 - 前端js校验了后端也要校验，因为前端js校验不安全可以跳过
        if(StringUtils.isEmpty(shop.getName())
            ||  StringUtils.isEmpty(shop.getAddress())
            ||  StringUtils.isEmpty(shop.getTel())
            ||  StringUtils.isEmpty(shop.getAdmin().getUsername())
            ||  StringUtils.isEmpty(shop.getAdmin().getPhone())
            ||  StringUtils.isEmpty(shop.getAdmin().getEmail())
            ||  StringUtils.isEmpty(shop.getAdmin().getPassword())
            ||  StringUtils.isEmpty(shop.getAdmin().getComfirmPassword())
        ){
            throw new BusinessException("店铺信息不能为空!");
        }
        //1.2.两次密码不一致校验 - 可以在Employee中加一个comfirmPassword字段，然后与密码判断是否相等
        if(!shop.getAdmin().getPassword().equals(shop.getAdmin().getComfirmPassword())){
            throw new BusinessException("密码不一致，请重新输入！");
        }
        //1.3.该店铺是否已经被入驻过 - 怎么判断入驻过？店铺名 + 地址
        Shop shopTemp = shopMapper.loadByNameAndAddress(shop);
        if(shopTemp != null){
            throw new BusinessException("店铺已经入驻，请直接登陆！");
        }
        //1.4.校验名称是否合规
        if(!BaiduAuditUtils.TextCensor(shop.getName())){
            throw new BusinessException("店铺名称不合法，请重新输入！");
        }
        //二：入驻业务 先添加表1，表1存在才可以添加表2数据以及外键，最后更新表2的外键
        //2.1.保存店铺管理员信息t_employee
        Employee admin = shop.getAdmin();
        employeeMapper.save(admin);
        //2.2.保存店铺信息t_shop
        shop.setAdmin_id(admin.getId());
        shopMapper.save(shop);
        //2.3.将店铺的id更新到t_employee中
        admin.setShop_id(shop.getId());
        employeeMapper.update(admin);
    }

    @Transactional
    @Override
    public void pass(ShopAuditLog log) throws MessagingException {
        //1.修改shop状态：state = 2
        Shop shop = shopMapper.loadById(log.getShop_id());
        shop.setState(2);
        shopMapper.update(shop);

        //2.添加审核日志到数据库，修改日志的state状态
        log.setState(2);
        log.setAudit_id(325L);
        shopAuditLogMapper.save(log);

        //3.发送邮件：审核通过邮件 - 叫他激活
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
        //设置发送方
        helper.setFrom("1091526909@qq.com");
        //设置主题
        helper.setSubject("店铺激活邮件");
        //设置内容
        helper.setText("<h1>你的店铺已经注册!!!</h1>" +
                "<img src='http://123.207.27.208"+shop.getLogo()+"' >" +
                "<a href='http://localhost:8080/shop/active/"+log.getShop_id()+"'>点击该链接激活</a>",true);
        //设置收件人
        Employee employee = employeeMapper.loadById(shop.getAdmin_id());
        //helper.setTo(employee.getEmail());
        helper.setTo("1091526909@qq.com");
        //发送
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void reject(ShopAuditLog log) throws MessagingException {
        //1.修改shop状态：state = 4
        Shop shop = shopMapper.loadById(log.getShop_id());
        shop.setState(4);
        shopMapper.update(shop);

        //2.添加审核日志到数据库，修改日志的state状态
        log.setState(4);
        log.setAudit_id(325L);
        shopAuditLogMapper.save(log);

        //3.发送邮件：审核通过邮件 - 叫他激活
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
        //设置发送方
        helper.setFrom("1091526909@qq.com");
        //设置主题
        helper.setSubject("入驻失败邮件");
        //设置内容
        helper.setText("<h1>入驻失败!!!</h1>" +
                "<a href='http://localhost:8081/#/register'>点击重新入驻</a>",true);
        //设置收件人
        Employee employee = employeeMapper.loadById(shop.getAdmin_id());
        //helper.setTo(employee.getEmail());
        helper.setTo("1091526909@qq.com");
        //发送
        javaMailSender.send(mimeMessage);
    }

    @Override
    public List<ShopDto> getCountByState() {
        return shopMapper.getCountByState();
    }
}
