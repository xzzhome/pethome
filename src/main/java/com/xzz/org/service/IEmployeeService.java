package com.xzz.org.service;

import com.xzz.basic.service.IBaseService;
import com.xzz.org.domain.Employee;

import java.util.List;

public interface IEmployeeService extends IBaseService<Employee>  {

    List<Employee> findByshopId(Long shopId);
}