package com.xzz.org.controller;

import com.xzz.basic.annotation.PreAuthorize;
import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.org.domain.Employee;
import com.xzz.org.query.EmployeeQuery;
import com.xzz.org.service.impl.EmployeeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/employee")
@Api(value = "员工的API",description="员工相关的CRUD功能")//接口文档的注解
public class EmployeeController {

    @Resource
    public EmployeeServiceImpl employeeService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    //@PreAuthorize(name = "通过ID查询员工列表",value = "employee:findOne")
    public Employee findOne(@PathVariable("id") Long id){
        return employeeService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    //@PreAuthorize(name = "查询员工列表",value = "employee:findAll")
    public List<Employee> findAll(){
        return employeeService.findAll();
    }

    @GetMapping("/list/{shopId}")
    @ApiOperation(value = "查询所有对象" )
    public List<Employee> findByshopId(@PathVariable("shopId") Long shopId){
        return employeeService.findByshopId(shopId);
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    //@PreAuthorize(name = "通过ID删除员工列表",value = "employee:delete")
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            employeeService.del(id);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("删除失败");
        }
    }

    //批量删除
    @PatchMapping
    @ApiOperation(value = "批量删除接口")
    //@PreAuthorize(name = "批量删除员工",value = "employee:patchDelete")
    public JsonResult patchDelete(@RequestBody List<Long> ids){
        try {
            employeeService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    //@PreAuthorize(name = "添加或修改员工列表",value = "employee:addOrUpdate")
    public JsonResult addOrUpdate(@RequestBody Employee employee){
        try {
            if(employee.getId()==null){
                employeeService.add(employee);
            }else{
                employeeService.update(employee);
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
    //@PreAuthorize(name = "分页查询或高级查询员工列表",value = "employee:queryPage")
    public PageList<Employee> queryPage(@RequestBody EmployeeQuery employeeQuery){
        return employeeService.queryPage(employeeQuery);
    }
}
