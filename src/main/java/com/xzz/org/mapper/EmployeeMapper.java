package com.xzz.org.mapper;

import com.xzz.org.domain.Employee;
import com.xzz.org.query.EmployeeQuery;

import java.util.List;

public interface EmployeeMapper {

    void save(Employee employee);

    void remove(Long id);

    void update(Employee employee);

    Employee loadById(Long id);

    List<Employee> loadAll();

    //分页：查询总数量
    Integer queryCount(EmployeeQuery query);

    //分页：查询当前页数据
    List<Employee> queryData(EmployeeQuery query);

    void patchDelete(List<Long> ids);
}