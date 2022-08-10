package com.xzz.pet.domain;

import com.xzz.basic.domain.BaseDomain;
import com.xzz.org.domain.Shop;
import com.xzz.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMasterMsg extends BaseDomain {
    private String title;
    private Integer state=0;
    private String name;
    private Integer age;
    private Integer gender;
    private String coatColor;
    private String resources;

    private Long petType;
    private PetType type;

    private BigDecimal price;
    private String address;

    private Long user_id;
    private User user;

    private Long shop_id;
    private Shop shop;

    private String note;
}
