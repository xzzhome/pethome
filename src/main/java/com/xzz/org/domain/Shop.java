package com.xzz.org.domain;

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
    private Date registerTime;
    private Integer state;
    private String address;
    private String logo;
    private Long admin_id;
}
