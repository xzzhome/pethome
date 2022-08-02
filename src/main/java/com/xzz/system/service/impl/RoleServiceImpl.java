package com.xzz.system.service.impl;


import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.system.domain.Role;
import com.xzz.system.mapper.RoleMapper;
import com.xzz.system.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Transactional
    @Override
    public void del(Long id) {
        //删除中间表：t_role_permission
        roleMapper.removeRolePermissionByRoldId(id);
        //删除中间表：t_role_menu
        roleMapper.removeRoleMenuByRoldId(id);
        //删除角色
        roleMapper.remove(id);
    }

    @Transactional
    @Override
    public void add(Role role) {
        //保存角色信息
        roleMapper.save(role);
        //操作相关信息：中间表信息
        initRelationTable(role);
    }

    @Transactional
    @Override
    public void update(Role role) {
        roleMapper.update(role);
        //删除中间表信息：t_role_permission
        roleMapper.removeRolePermissionByRoldId(role.getId());
        //删除中间表信息：t_role_menu
        roleMapper.removeRoleMenuByRoldId(role.getId());
        initRelationTable(role);
    }

    private void initRelationTable(Role role) {
        Long roleId = role.getId();
        //角色分配权限
        List<Long> permissions = role.getPermissions();
        if(permissions.size() > 0){
            //添加中间表信息：t_role_permission
            roleMapper.saveRolePermissons(roleId, permissions);
        }

        //角色分配菜单
        List<Long> menus = role.getMenus();
        if(menus.size() > 0) {
            //添加中间表信息：t_role_menu
            roleMapper.saveRoleMenus(roleId, menus);
        }
    }
    //批量删除重构
    @Override
    public void patchDelete(List<Long> ids) {
        for (Long id : ids) {
            del(id);
        }
        //有外键关联 - 先删除关联数据 - 在删主表数据
        //super.patchDelete(ids);不需要了 - del(id)中已经删除了
    }
}
