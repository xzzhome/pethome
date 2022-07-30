package com.xzz.user.service;

import com.xzz.basic.service.IBaseService;
import com.xzz.basic.util.JsonResult;
import com.xzz.user.domain.Logininfo;
import com.xzz.user.dto.LoginDto;

public interface ILogininfoService extends IBaseService<Logininfo> {
    JsonResult accountLogin(LoginDto loginDto);
}
