package com.xzz.org.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Shop {
    private Long id;
    private String name;
    private String tel;
    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date registerTime = new Date();
    private Integer state=1;
    private String address;
    private String logo;
    //店长ID
    private Long admin_id;
    private Employee admin;
}
