package com.xzz.order.service;

import com.xzz.basic.query.PageList;
import com.xzz.basic.service.IBaseService;
import com.xzz.order.domain.OrderPetAcquisition;
import com.xzz.order.dto.OrderDto;
import com.xzz.order.query.OrderPetAcquisitionQuery;
import com.xzz.pet.domain.PetLog;

import javax.servlet.http.HttpServletRequest;

public interface IOrderPetAcquisitionService extends IBaseService<OrderPetAcquisition>  {

    PageList<OrderPetAcquisition> queryPage(OrderPetAcquisitionQuery query, HttpServletRequest request);

    void confirm(OrderDto orderDto);
}