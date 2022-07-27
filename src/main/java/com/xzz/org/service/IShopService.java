package com.xzz.org.service;

import com.xzz.basic.service.IBaseService;
import com.xzz.org.domain.Employee;
import com.xzz.org.domain.Shop;
import com.xzz.org.domain.ShopAuditLog;
import com.xzz.org.dto.ShopDto;

import javax.mail.MessagingException;
import java.util.List;

public interface IShopService extends IBaseService<Shop>  {

    void settlement(Shop shop);

    void pass(ShopAuditLog log) throws MessagingException;

    void reject(ShopAuditLog log) throws MessagingException;

    List<ShopDto> getCountByState();
}