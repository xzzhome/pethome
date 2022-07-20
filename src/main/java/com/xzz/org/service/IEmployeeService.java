package com.xzz.org.service;

import com.xzz.basic.query.PageList;
import com.xzz.org.domain.Employee;
import com.xzz.org.query.EmployeeQuery;

import java.util.List;

public interface IEmployeeService {

    void add(Employee employee);

    void del(Long id);

    void update(Employee employee);

    Employee findById(Long id);

    List<Employee> findAll();

    //分页查询，之后传给页面的    数量和    数据
    PageList<Employee> queryPage(EmployeeQuery query);

    void patchDelete(List<Long> ids);
}