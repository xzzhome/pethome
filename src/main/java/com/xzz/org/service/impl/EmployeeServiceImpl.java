package com.xzz.org.service.impl;


import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.org.domain.Employee;
import com.xzz.org.service.IEmployeeService;
import org.springframework.stereotype.Service;


@Service
//@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)事务具有继承性
public class EmployeeServiceImpl extends BaseServiceImpl<Employee> implements IEmployeeService {

}
