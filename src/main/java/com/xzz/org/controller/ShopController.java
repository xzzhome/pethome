package com.xzz.org.controller;

import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.org.domain.Shop;
import com.xzz.org.query.ShopQuery;
import com.xzz.org.service.impl.ShopServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shop")
@Api(value = "店铺的API",description="店铺相关的CRUD功能")//接口文档的注解
public class ShopController {

    @Resource
    public ShopServiceImpl shopService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public Shop findOne(@PathVariable("id") Long id){
        return shopService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<Shop> findAll(){
        return shopService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            shopService.del(id);
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
            shopService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody Shop shop){
        try {
            if(shop.getId()==null){
                shopService.add(shop);
            }else{
                shopService.update(shop);
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
    public PageList<Shop> queryPage(@RequestBody ShopQuery shopQuery){
        return shopService.queryPage(shopQuery);
    }

    //商家入驻接口 //先写主体，再写细节
    @ApiOperation(value = "商家入驻",notes = "")
    @PostMapping("/settlement")
    public JsonResult settlement(@RequestBody Shop shop){
        try {
            shopService.settlement(shop);
            return JsonResult.me();
        } catch (BusinessException e){ //业务异常捕获
            return JsonResult.me().setMsg("入驻失败!");
        } catch (Exception e) { //500
            e.printStackTrace();
            return JsonResult.me().setMsg("系统异常,请稍后重试!");
        }
    }
}
