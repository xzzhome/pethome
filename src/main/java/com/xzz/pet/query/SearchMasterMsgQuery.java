package com.xzz.pet.query;

import com.xzz.basic.query.BaseQuery;
import lombok.Data;

@Data
public class SearchMasterMsgQuery extends BaseQuery {
    private Integer state;
    private Long shop_id;
    private Long user_id;
}