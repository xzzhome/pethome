package com.xzz;

import com.xzz.org.domain.Department;
import com.xzz.org.mapper.DepartmentMapper;
import com.xzz.org.query.DepartmentQuery;
import com.xzz.org.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= PetHomeApp.class)
class DepartmentTest {

    @Resource
    private DepartmentServiceImpl service;
    @Resource
    private DepartmentMapper mapper;

    @Test
    void add() {
    }

    @Test
    void del() {
        service.del(23L);
    }

    @Test
    void update() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
        List<Department> list = service.findAll();
        System.out.println(list);
    }

    @Test
    void queryPage() {

    }


}