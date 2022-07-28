package com.xzz.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logininfo  {

    private Long id;
    
    private String username;
    
    private String phone;
    
    private String email;
    
    private String salt;
    
    private String password;
    /**
     * 类型 - 0管理员，1用户
     */
    private Integer type;
    /**
     * 启用状态：true可用，false禁用
     */
    private Boolean disable=true;



}

