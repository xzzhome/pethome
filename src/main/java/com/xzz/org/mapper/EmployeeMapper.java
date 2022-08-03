package com.xzz.org.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.org.domain.Employee;

import java.util.List;

public interface EmployeeMapper extends BaseMapper<Employee> {


    void removeEmployeeRoleByEmployeeId(Long id);

    void saveEmployeeRole(Employee employee);

    List<String> loadPerssionSnByLogininfoId(Long id);
}