package com.xzz.pet.controller;

import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.pet.domain.PetType;
import com.xzz.pet.query.PetTypeQuery;
import com.xzz.pet.service.impl.PetTypeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/petType")
@Api(value = "店铺的API",description="店铺相关的CRUD功能")//接口文档的注解
public class PetTypeController {

    @Resource
    public PetTypeServiceImpl petTypeService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public PetType findOne(@PathVariable("id") Long id){
        return petTypeService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<PetType> findAll(){
        return petTypeService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            petTypeService.del(id);
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
            petTypeService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody PetType petType){
        try {
            if(petType.getId()==null){
                petTypeService.add(petType);
            }else{
                petTypeService.update(petType);
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
    public PageList<PetType> queryPage(@RequestBody PetTypeQuery petTypeQuery){
        return petTypeService.queryPage(petTypeQuery);
    }

    //查询部门树
    @GetMapping("/typeTree")
    public List<PetType> typeTree(){
        return petTypeService.typeTree();
    }

}
