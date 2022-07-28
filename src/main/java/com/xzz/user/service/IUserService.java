package com.xzz.user.service;

import com.xzz.basic.service.IBaseService;
import com.xzz.user.domain.User;
import com.xzz.user.dto.UserDto;

public interface IUserService extends IBaseService<User> {
    void phoneRegister(UserDto userDto);
}
