package com.xzz.user.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.user.domain.User;

public interface UserMapper extends BaseMapper<User> {
    User loadByPhone(String phone);

    User loadByLogininfoId(Long id);
}
