package com.xzz.system.service;

import com.xzz.basic.service.IBaseService;
import com.xzz.system.domain.Menu;
import com.xzz.system.domain.SystemType;

import java.util.List;

public interface IMenuService extends IBaseService<Menu> {
    List<Menu> menuTree();
}
