package com.xzz.pet.query;

import com.xzz.basic.query.BaseQuery;
import lombok.Data;

@Data
public class PetQuery extends BaseQuery {
    //上下架状态
    private Integer state;

    private Long shopId;
}