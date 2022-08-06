package com.xzz.pet.service;

import com.xzz.basic.service.IBaseService;
import com.xzz.basic.util.JsonResult;
import com.xzz.org.domain.Shop;
import com.xzz.org.domain.ShopAuditLog;
import com.xzz.org.dto.ShopDto;
import com.xzz.pet.domain.Pet;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IPetService extends IBaseService<Pet>  {


    JsonResult onsale(List<Long> ids, HttpServletRequest request);

    JsonResult offsale(List<Long> ids, HttpServletRequest request);
}