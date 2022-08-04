package com.xzz.system.controller;

import com.xzz.basic.annotation.PreAuthorize;
import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.system.domain.Menu;
import com.xzz.system.domain.Menu;
import com.xzz.system.domain.SystemType;
import com.xzz.system.query.MenuQuery;
import com.xzz.system.query.MenuQuery;
import com.xzz.system.query.SystemTypeQuery;
import com.xzz.system.service.IMenuService;
import com.xzz.system.service.impl.MenuServiceImpl;
import com.xzz.system.service.impl.SystemTypeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    public IMenuService menuService;

    /**
     * 添加或修改
     * @param menu  传递的实体
     * @return Ajaxresult转换结果
     */
    @PutMapping
    //@PreAuthorize(name = "添加或修改权限菜单列表",value = "menu:addOrUpdate")
    public JsonResult addOrUpdate(@RequestBody Menu menu){
        try {
            if( menu.getId()!=null)
                menuService.update(menu);
            else
                menuService.add(menu);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("操作失败");
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping(value="/{id}")
    @PreAuthorize(name = "删除菜单",value = "menu:delete")
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            menuService.del(id);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("删除失败");
        }
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "批量删除接口")
    //@PreAuthorize(name = "通过ID批量删除权限菜单列表",value = "menu:patchDelete")
    public JsonResult patchDelete(@RequestBody List<Long> ids){
        try {
            menuService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("删除失败");
        }
    }


    /**
     * 查询单个信息
     * @param id
     */
    @GetMapping("/{id}")
    //@PreAuthorize(name = "通过ID查询权限菜单列表",value = "menu:findOne")
    public Menu findOne(@PathVariable("id")Long id) {
        return menuService.findById(id);
    }


    /**
     * 查询所有信息
     * @return
     */
    @GetMapping
    //@PreAuthorize(name = "查询所有权限菜单列表",value = "menu:findAll")
    public List<Menu> findAll(){
        return menuService.findAll();
    }


    /**
     * 分页查询或高级查询
     * @param query 查询对象
     * @return PageList 分页对象
     */
    @PostMapping
    //@PreAuthorize(name = "分页查询或高级查询权限菜单",value = "menu:queryPage")
    public PageList<Menu> queryPage(@RequestBody MenuQuery query) {
        return menuService.queryPage(query);
    }


    /**
     * 查询菜单树
     * @return
     */
    @GetMapping("/menuTree")
    @ApiOperation(value = "获取菜单树-无限极接口")
    //@PreAuthorize(name = "查询菜单树",value = "menu:menuTree")
    public List<Menu> menuTree(){
        return menuService.menuTree();
    }
}

