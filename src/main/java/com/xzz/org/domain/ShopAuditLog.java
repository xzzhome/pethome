package com.xzz.org.domain;

import com.xzz.basic.domain.BaseDomain;
import lombok.Data;

import java.util.Date;

@Data
public class ShopAuditLog extends BaseDomain {
    private Integer state;  // 状态
    private Long shop_id;  // 店铺id
    private Long audit_id; // 审核人（现在做不了）
    private Date audit_time = new Date();  // 审核时间
    private String note;  // 内容

}