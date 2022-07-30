package com.xzz.user.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.user.domain.Logininfo;
import com.xzz.user.dto.LoginDto;

public interface LogininfoMapper extends BaseMapper<Logininfo> {
    Logininfo loadByAccount(LoginDto loginDto);
}
