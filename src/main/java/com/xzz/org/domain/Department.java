package com.xzz.org.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Department{
    //部门ID
    private Long id;
    //部门编号
    private String sn;
    //部门名称
    private String name;
    //部门路径
    private String dirPath;
    //部门状态：1启用,0禁用
    private Integer state = 1;
    //部门经理ID和部门经理
    private Long manager_id;
    private Employee manager;

    //上级部门ID和上级部门
    private Long parent_id;
    private Department parent;

    //上级部门下的子部门（多对一）
    private List<Department> children = new ArrayList<>();
}