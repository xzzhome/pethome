package com.xzz.system.controller;

import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.system.domain.SystemDetail;
import com.xzz.system.query.SystemDetailQuery;
import com.xzz.system.service.impl.SystemDetailServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/systemDetail")
@Api(value = "数据明细的API",description="数据明细相关的CRUD功能")//接口文档的注解
public class SystemDetailController {

    @Resource
    public SystemDetailServiceImpl systemDetailService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public SystemDetail findOne(@PathVariable("id") Long id){
        return systemDetailService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<SystemDetail> findAll(){
        return systemDetailService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            systemDetailService.del(id);
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
            systemDetailService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody SystemDetail systemDetail){
        try {
            if(systemDetail.getId()==null){
                systemDetailService.add(systemDetail);
            }else{
                systemDetailService.update(systemDetail);
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
    public PageList<SystemDetail> queryPage(@RequestBody SystemDetailQuery systemDetailQuery){
        return systemDetailService.queryPage(systemDetailQuery);
    }
}
