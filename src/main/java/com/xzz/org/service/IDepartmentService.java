package com.xzz.org.service;

import com.xzz.basic.service.IBaseService;
import com.xzz.org.domain.Department;
import java.util.List;

public interface IDepartmentService extends IBaseService<Department> {

    //无限级数
    List<Department> deptTree();
}