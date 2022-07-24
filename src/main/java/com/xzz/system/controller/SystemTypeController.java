package com.xzz.system.controller;

import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.org.domain.Department;
import com.xzz.system.domain.SystemDetail;
import com.xzz.system.domain.SystemType;
import com.xzz.system.query.SystemTypeQuery;
import com.xzz.system.service.impl.SystemTypeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/systemType")
@Api(value = "数据类型的API",description="数据类型相关的CRUD功能")//接口文档的注解
public class SystemTypeController {

    @Resource
    public SystemTypeServiceImpl systemTypeService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public SystemType findOne(@PathVariable("id") Long id){
        return systemTypeService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<SystemType> findAll(){
        return systemTypeService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            systemTypeService.del(id);
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
            systemTypeService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody SystemType systemType){
        try {
            if(systemType.getId()==null){
                systemTypeService.add(systemType);
            }else{
                systemTypeService.update(systemType);
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
    public PageList<SystemType> queryPage(@RequestBody SystemTypeQuery systemTypeQuery){
        return systemTypeService.queryPage(systemTypeQuery);
    }

    //查询部门树
    /*@GetMapping("/deptTree")
    @ApiOperation(value = "获取部门树-无限极接口")
    public List<SystemType> deptTree(){
        return systemTypeService.deptTree();
    }*/
}
