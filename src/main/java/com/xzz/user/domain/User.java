package com.xzz.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    
    private String username;
    
    private String phone;
    
    private String email;
    /**
     * 盐值
     */
    private String salt;
    /**
     * 密码，md5加密加盐
     */
    private String password;
    /**
     * 员工状态 - 1启用，0禁用
     */
    private Integer state=1;
    
    private Integer age;

    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private Date createtime = new Date();
    
    private String headImg;
    
    private Long logininfo_id;
    private Logininfo logininfo;

}

