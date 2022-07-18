package com.xzz.org.mapper;

import com.xzz.org.domain.Department;
import com.xzz.org.query.DepartmentQuery;

import java.util.List;

public interface DepartmentMapper {

    void save(Department department);

    void remove(Long id);

    void update(Department department);

    Department loadById(Long id);

    List<Department> loadAll();

    //分页：查询总数量
    Integer queryCount(DepartmentQuery query);

    //分页：查询当前页数据
    List<Department> queryData(DepartmentQuery query);

    void patchDelete(List<Long> ids);
}