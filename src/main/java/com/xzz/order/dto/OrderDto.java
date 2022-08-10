package com.xzz.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDto {
    private Long id;
    private Integer payType;
    private BigDecimal money;
}
