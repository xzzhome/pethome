package com.xzz.user.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.user.domain.User;
import com.xzz.user.domain.Wxuser;

public interface WxUserMapper extends BaseMapper<Wxuser> {

    Wxuser loadByOpenid(String openid);
}
