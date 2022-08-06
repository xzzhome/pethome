package com.xzz.pet.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xzz.basic.domain.BaseDomain;
import com.xzz.org.domain.Shop;
import com.xzz.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet extends BaseDomain {
    private String name;
    private BigDecimal costprice;
    private BigDecimal saleprice;

    private Long type_id;
    private PetType type;

    private String resources;
    private Integer state=0;
    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date offsaletime;
    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date onsaletime;
    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date createtime = new Date();

    private Long shop_id;
    private Shop shop;

    private Long user_id;
    private User user;

    private Long search_master_msg_id;

    private PetDetail petDetail;
}
