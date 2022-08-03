package com.xzz.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xzz.basic.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseDomain {
    //菜单名称
    private String name;
    //菜单组件路径：例如/org/Department
    private String component;
    //路径：路由中当前组件的path路径
    private String url;
    //图标
    private String icon;
    //排序索引
    private Integer index;
    //父菜单id
    private Long parent_id;
    //父级菜单
    private Menu parent;
    //简介
    private String intro;
    //状态
    private Boolean state = true;
    //当前部门下的子部门
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children = new ArrayList<>();
}