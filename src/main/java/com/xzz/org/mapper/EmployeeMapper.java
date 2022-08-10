package com.xzz.org.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.org.domain.Employee;
import com.xzz.system.domain.Menu;

import java.util.List;

public interface EmployeeMapper extends BaseMapper<Employee> {


    void removeEmployeeRoleByEmployeeId(Long id);

    void saveEmployeeRole(Employee employee);

    List<String> loadPerssionSnByLogininfoId(Long id);

    List<Menu> loadMenusByLogininfoId(Long id);

    Employee loadByLogininfoId(Long id);

    List<Employee> findByshopId(Long shopId);

}