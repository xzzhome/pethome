package com.xzz.system.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.system.domain.Permission;
import com.xzz.system.domain.SystemDetail;

public interface PermissionMapper extends BaseMapper<Permission> {
    Permission loadBySn(String permissionSn);
}
