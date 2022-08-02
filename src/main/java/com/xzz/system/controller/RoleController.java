package com.xzz.system.controller;

import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.system.domain.Role;
import com.xzz.system.query.RoleQuery;
import com.xzz.system.service.impl.RoleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/role")
@Api(value = "权限角色的API",description="权限角色相关的CRUD功能")//接口文档的注解
public class RoleController {

    @Resource
    public RoleServiceImpl roleService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public Role findOne(@PathVariable("id") Long id){
        return roleService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<Role> findAll(){
        return roleService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            roleService.del(id);
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
            roleService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody Role role){
        try {
            if(role.getId()==null){
                roleService.add(role);
            }else{
                roleService.update(role);
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
    public PageList<Role> queryPage(@RequestBody RoleQuery roleQuery){
        return roleService.queryPage(roleQuery);
    }
}
