package com.xzz.org.service.impl;

import com.xzz.basic.PageList;
import com.xzz.org.domain.Department;
import com.xzz.org.mapper.DepartmentMapper;
import com.xzz.org.query.DepartmentQuery;
import com.xzz.org.service.IDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class DepartmentServiceImpl implements IDepartmentService {

    @Resource
    private DepartmentMapper mapper;

    @Transactional
    @Override
    public void add(Department department) {
        mapper.save(department);
    }

    @Transactional
    @Override
    public void del(Long id) {
        mapper.remove(id);
    }

    @Transactional
    @Override
    public void update(Department department) {
        mapper.update(department);
    }

    @Override
    public Department findById(Long id) {
        return mapper.loadById(id);
    }

    @Override
    public List<Department> findAll() {
        return mapper.loadAll();
    }

    @Override
    public PageList<Department> queryPage(DepartmentQuery query) {
        Integer totals = mapper.queryCount(query);
        List<Department> data = mapper.queryData(query);
        return new PageList<>(totals,data);
    }
}
