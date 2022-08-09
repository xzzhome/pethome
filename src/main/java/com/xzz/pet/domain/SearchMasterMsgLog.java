package com.xzz.pet.domain;

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
public class SearchMasterMsgLog extends BaseDomain {
    private Long msg_id;
    private Integer state;
    private String audit_id;
    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date audit_time = new Date();;
    private String note;
}
