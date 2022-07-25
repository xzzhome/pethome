package com.xzz.org.service;

import com.xzz.basic.service.IBaseService;
import com.xzz.org.domain.Employee;
import com.xzz.org.domain.Shop;
import com.xzz.org.domain.ShopAuditLog;

import javax.mail.MessagingException;

public interface IShopService extends IBaseService<Shop>  {

    void settlement(Shop shop);

    void pass(ShopAuditLog log) throws MessagingException;

    void reject(ShopAuditLog log) throws MessagingException;
}