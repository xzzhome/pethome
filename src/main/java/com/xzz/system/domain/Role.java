package com.xzz.system.domain;

import com.xzz.basic.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseDomain {
    private String name;
    private String sn;
    // 角色和权限 多对多关系：当前角色拥有的权限 - 接收前端传递的权限数据
    private List<Long> permissions = new ArrayList<>();
    // 角色和菜单 多对多关系：当前角色拥有的菜单 - 接收前端传递的菜单数据
    private List<Long> menus = new ArrayList<>();
    // 以后做菜单回显可以加这个参数
    private List<Menu> ownMenus = new ArrayList<>();
}