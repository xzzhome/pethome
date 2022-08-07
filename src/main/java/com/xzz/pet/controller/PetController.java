package com.xzz.pet.controller;

import com.xzz.basic.annotation.PreAuthorize;
import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.query.PageList;
import com.xzz.basic.util.ExcelUtils;
import com.xzz.basic.util.JsonResult;
import com.xzz.org.domain.Department;
import com.xzz.pet.domain.Pet;
import com.xzz.pet.domain.PetType;
import com.xzz.pet.query.PetQuery;
import com.xzz.pet.service.IPetTypeService;
import com.xzz.pet.service.impl.PetServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/pet")
@Api(value = "宠物的API",description="宠物相关的CRUD功能")//接口文档的注解
public class PetController {

    @Resource
    public PetServiceImpl petService;
    @Autowired
    private IPetTypeService petTypeService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public Pet findOne(@PathVariable("id") Long id){
        return petService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<Pet> findAll(){
        return petService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            petService.del(id);
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
            petService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody Pet pet){
        try {
            if(pet.getId()==null){
                petService.add(pet);
            }else{
                petService.update(pet);
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
    public PageList<Pet> queryPage(@RequestBody PetQuery petQuery){
        return petService.queryPage(petQuery);
    }

    //上架接口
    @PostMapping("/onsale")
    public JsonResult onsale(@RequestBody List<Long> ids){
        try {
            return petService.onsale(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("上架失败！"+e.getMessage());
        }
    }

    //下架接口
    @PostMapping("/offsale")
    public JsonResult offsale(@RequestBody List<Long> ids,HttpServletRequest request){
        try {
            return petService.offsale(ids,request);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("下架失败！"+e.getMessage());
        }
    }

}
