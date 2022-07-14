package com.xzz.org.service;

import com.xzz.basic.PageList;
import com.xzz.org.domain.Department;
import com.xzz.org.query.DepartmentQuery;

import java.util.List;

public interface IDepartmentService {

    void add(Department department);

    void del(Long id);

    void update(Department department);

    Department findById(Long id);

    List<Department> findAll();

    //分页查询，之后传给页面的    数量和    数据
    PageList<Department> queryPage(DepartmentQuery query);
}