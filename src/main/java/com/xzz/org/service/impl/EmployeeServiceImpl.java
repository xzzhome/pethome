package com.xzz.org.service.impl;


import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.basic.util.Md5Utils;
import com.xzz.basic.util.StrUtils;
import com.xzz.org.domain.Employee;
import com.xzz.org.mapper.EmployeeMapper;
import com.xzz.org.service.IEmployeeService;
import com.xzz.user.domain.Logininfo;
import com.xzz.user.mapper.LogininfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
//@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)事务具有继承性
public class EmployeeServiceImpl extends BaseServiceImpl<Employee> implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private LogininfoMapper logininfoMapper;

    @Override
    public void del(Long id) {
        //删除登录信息表数据
        Employee employee = employeeMapper.loadById(id);
        logininfoMapper.remove(employee.getLogininfo_id());

        //删除t_employee:删除中间表信息：t_employee_role
        employeeMapper.removeEmployeeRoleByEmployeeId(id);

        //删除employee表数据
        super.del(id);
    }

    @Override
    public void add(Employee employee) {
        //获取随机盐值
        String salt = StrUtils.getComplexRandomString(32);
        //获取加密之后的密码
        String md5Pwd = Md5Utils.encrypByMd5(employee.getPassword()+salt);
        employee.setSalt(salt);
        employee.setPassword(md5Pwd);
        //添加的emp信息同步到logininfo表中
        Logininfo logininfo = employee2Logininfo(employee);
        logininfoMapper.save(logininfo);

        employee.setLogininfo_id(logininfo.getId());
        super.add(employee);

        //保存中间表的信息
        employeeMapper.saveEmployeeRole(employee);

    }

    @Override
    public void update(Employee employee) {
        //1.处理密码和盐值
        //获取随机盐值
        String salt = StrUtils.getComplexRandomString(32);
        //获取加密之后的密码
        String md5Pwd = Md5Utils.encrypByMd5(employee.getPassword()+salt);
        employee.setSalt(salt);
        employee.setPassword(md5Pwd);
        Logininfo logininfo = employee2Logininfo(employee);

        //Long logininfo_id = employeeMapper.loadById(employee.getId()).getLogininfo_id();
        //logininfo.setId(logininfo_id);
        logininfoMapper.update(logininfo);

        //登录信息id和店铺信息id不能修改
        super.update(employee);

        //删除中间表信息
        employeeMapper.removeEmployeeRoleByEmployeeId(employee.getId());
        //保存新的信息
        employeeMapper.saveEmployeeRole(employee);
    }

    private Logininfo employee2Logininfo(Employee employee) {
        Logininfo logininfo = new Logininfo();
        BeanUtils.copyProperties(employee,logininfo);
        logininfo.setType(0);
        return logininfo;
    }

    //批量删除
    @Override
    public void patchDelete(List<Long> ids) {
        for (Long id : ids) {
            del(id);
        }
    }
}
