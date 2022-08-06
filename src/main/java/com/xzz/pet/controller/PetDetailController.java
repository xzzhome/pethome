package com.xzz.pet.controller;

import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.pet.domain.PetDetail;
import com.xzz.pet.query.PetDetailQuery;
import com.xzz.pet.service.impl.PetDetailServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/petDetail")
@Api(value = "店铺的API",description="店铺相关的CRUD功能")//接口文档的注解
public class PetDetailController {

    @Resource
    public PetDetailServiceImpl petDetailService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public PetDetail findOne(@PathVariable("id") Long id){
        return petDetailService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<PetDetail> findAll(){
        return petDetailService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            petDetailService.del(id);
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
            petDetailService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody PetDetail petDetail){
        try {
            if(petDetail.getId()==null){
                petDetailService.add(petDetail);
            }else{
                petDetailService.update(petDetail);
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
    public PageList<PetDetail> queryPage(@RequestBody PetDetailQuery petDetailQuery){
        return petDetailService.queryPage(petDetailQuery);
    }

}
