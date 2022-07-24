package com.xzz.org.service.impl;


import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.org.domain.Employee;
import com.xzz.org.domain.Shop;
import com.xzz.org.mapper.EmployeeMapper;
import com.xzz.org.mapper.ShopMapper;
import com.xzz.org.service.IEmployeeService;
import com.xzz.org.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class ShopServiceImpl extends BaseServiceImpl<Shop> implements IShopService {

    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

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
}
