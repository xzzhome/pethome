package com.xzz.system.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.system.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    void removeRolePermissionByRoldId(Long id);

    void removeRoleMenuByRoldId(Long id);

    //添加中间表信息：t_role_permission
    void saveRolePermissons(@Param("id")Long roleId, @Param("permissions")List<Long> permissions);
    //添加中间表信息：t_role_menu
    void saveRoleMenus(@Param("id")Long roleId, @Param("menus")List<Long> menus);

    void removeEmpRoleByRoldId(Long id);
}
