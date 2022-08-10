package com.xzz.order.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.order.domain.OrderPetAcquisition;
import com.xzz.pet.domain.PetLog;


public interface OrderPetAcquisitionMapper extends BaseMapper<OrderPetAcquisition> {


    OrderPetAcquisition loadByMsgId(Long msgId);
}