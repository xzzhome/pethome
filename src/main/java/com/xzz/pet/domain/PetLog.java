package com.xzz.pet.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xzz.basic.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetLog extends BaseDomain {
    private Long pet_id;
    private Integer state;
    private Long audit_id;
    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date audit_time = new Date();
    private String note;
}
