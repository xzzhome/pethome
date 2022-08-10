package com.xzz.order.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xzz.basic.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPetAcquisition extends BaseDomain {
    private String orderSn;
    private String digest;
    private Date lastcomfirmtime;
    private Integer state;
    private BigDecimal price;
    private Integer paytype;
    private Long pet_id;
    private Long user_id;
    private Long shop_id;
    private String address;
    private Long emp_id;
    private Long search_master_msg_id;

}
