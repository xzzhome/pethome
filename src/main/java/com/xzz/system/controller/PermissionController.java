package com.xzz.system.controller;

import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.system.domain.Permission;
import com.xzz.system.query.PermissionQuery;
import com.xzz.system.service.impl.PermissionServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/permission")
@Api(value = "权限的API",description="权限相关的CRUD功能")//接口文档的注解
public class PermissionController {

    @Resource
    public PermissionServiceImpl permissionService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public Permission findOne(@PathVariable("id") Long id){
        return permissionService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<Permission> findAll(){
        return permissionService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            permissionService.del(id);
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
            permissionService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody Permission permission){
        try {
            if(permission.getId()==null){
                permissionService.add(permission);
            }else{
                permissionService.update(permission);
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
    public PageList<Permission> queryPage(@RequestBody PermissionQuery permissionQuery){
        return permissionService.queryPage(permissionQuery);
    }
}
