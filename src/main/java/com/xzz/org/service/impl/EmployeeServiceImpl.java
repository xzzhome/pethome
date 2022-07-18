package com.xzz.org.service.impl;

import com.xzz.basic.PageList;
import com.xzz.org.domain.Employee;
import com.xzz.org.mapper.EmployeeMapper;
import com.xzz.org.query.EmployeeQuery;
import com.xzz.org.service.IEmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class EmployeeServiceImpl implements IEmployeeService {

    @Resource
    private EmployeeMapper mapper;

    @Transactional
    @Override
    public void add(Employee employee) {
        mapper.save(employee);
    }

    @Transactional
    @Override
    public void del(Long id) {
        mapper.remove(id);
    }

    @Transactional
    @Override
    public void update(Employee employee) {
        mapper.update(employee);
    }

    @Override
    public Employee findById(Long id) {
        return mapper.loadById(id);
    }

    @Override
    public List<Employee> findAll() {
        return mapper.loadAll();
    }

    @Override
    public PageList<Employee> queryPage(EmployeeQuery query) {
        Integer totals = mapper.queryCount(query);
        if (totals==null || totals==0){
            return  new PageList<>();
        }
        List<Employee> data = mapper.queryData(query);
        return new PageList<>(totals,data);
    }

    @Transactional
    @Override
    public void patchDelete(List<Long> ids) {
        mapper.patchDelete(ids);
    }
}
