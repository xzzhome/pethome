package com.xzz.org.controller;

import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.query.PageList;
import com.xzz.basic.util.ExcelUtils;
import com.xzz.basic.util.JsonResult;
import com.xzz.org.domain.Shop;
import com.xzz.org.domain.ShopAuditLog;
import com.xzz.org.dto.ShopDto;
import com.xzz.org.query.ShopQuery;
import com.xzz.org.service.impl.ShopServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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

    //商家入驻接口
    @ApiOperation(value = "商家入驻",notes = "")
    @PostMapping("/settlement")
    public JsonResult settlement(@RequestBody Shop shop){
        try {
            shopService.settlement(shop);
            return JsonResult.me();
        } catch (BusinessException e){ //业务异常捕获
            return JsonResult.me().setMsg(e.getMessage());
        } catch (Exception e) { //500
            e.printStackTrace();
            return JsonResult.me().setMsg("系统异常,请稍后重试!");
        }
    }

    //接口：店铺审核通过
    @PostMapping("/audit/pass")
    @ApiOperation(value = "店铺审核通过")
    public JsonResult pass(@RequestBody ShopAuditLog log){
        try {
            shopService.pass(log);
            return JsonResult.me();//true + "操作成功"
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("审核失败");
        }
    }

    //接口：店铺审核驳回
    @PostMapping("/audit/reject")
    @ApiOperation(value = "店铺审核驳回")
    public JsonResult reject(@RequestBody ShopAuditLog log){
        try {
            shopService.reject(log);
            return JsonResult.me();//true + "操作成功"
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("驳回失败");
        }
    }

    //导出
    @GetMapping("/export")
    public void export( HttpServletResponse response){
        try {
            List<Shop> list = shopService.findAll();
            ExcelUtils.exportExcel(list, null, "店铺信息", Shop.class, "shop.xlsx", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //导入
    @PostMapping("/importExcel")
    public void importExcel(@RequestPart("file") MultipartFile file){
        List<Shop> list = ExcelUtils.importExcel(file,0,1,Shop.class);
        for (Shop shop:list) {
            System.out.println(shop);
        }
    }

    //报表
    @GetMapping("/echarts")
    public List<ShopDto> echartsData(){
        return shopService.getCountByState();
    }
}
