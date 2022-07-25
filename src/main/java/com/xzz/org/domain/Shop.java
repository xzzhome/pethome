package com.xzz.org.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "店铺名称",width = 30)
    private String name;
    @Excel(name = "店铺电话",width = 30)
    private String tel;
    @Excel(name = "入驻时间",width = 30,exportFormat="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date registerTime = new Date();
    @Excel(name = "店铺状态",width = 30)
    private Integer state=1;
    @Excel(name = "店铺地址",width = 30)
    private String address;
    @Excel(name = "店铺logo",width = 30)
    private String logo;
    //店长ID
    @Excel(name = "店长id",width = 30)
    private Long admin_id;
    private Employee admin;
}
