package com.xzz.org.controller;

import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.org.domain.Department;
import com.xzz.org.query.DepartmentQuery;
import com.xzz.org.service.impl.DepartmentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/department")
@Api(value = "部门的API",description="部门相关的CRUD功能")//接口文档的注解
public class DepartmentController {

    @Resource
    public DepartmentServiceImpl departmentService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public Department findOne(@PathVariable("id") Long id){
        return departmentService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<Department> findAll(){
        return departmentService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            departmentService.del(id);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("删除失败");
        }
    }

    //批量删除
    @PatchMapping
    @ApiOperation(value = "批量删除接口")
    public JsonResult patchDelete(@RequestBody List<Long> ids){
        try {
            departmentService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody Department department){
        try {
            if(department.getId()==null){
                departmentService.add(department);
            }else{
                departmentService.update(department);
            }
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("操作失败");
        }
    }

    //分页查询或高级查询
    @PostMapping
    @ApiOperation(value = "分页查询或高级查询" )
    public PageList<Department> queryPage(@RequestBody DepartmentQuery departmentQuery){
        return departmentService.queryPage(departmentQuery);
    }

    //查询部门树
    @GetMapping("/deptTree")
    @ApiOperation(value = "获取部门树-无限极接口")
    public List<Department> deptTree(){
        return departmentService.deptTree();
    }
}
