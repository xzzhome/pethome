package com.xzz.user.domain;

import com.xzz.basic.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wxuser extends BaseDomain {
    private String openid;
    private String nickname;
    private Integer sex;
    private String address;
    private String headimgurl;
    private String unionid;
    private Long user_id;

}

