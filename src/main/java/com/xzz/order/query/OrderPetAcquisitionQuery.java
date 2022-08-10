package com.xzz.order.query;

import com.xzz.basic.query.BaseQuery;
import lombok.Data;

@Data
public class OrderPetAcquisitionQuery extends BaseQuery {
    private Long EmpId;
    private Long shopId;
}