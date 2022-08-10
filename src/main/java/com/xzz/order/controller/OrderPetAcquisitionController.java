package com.xzz.order.controller;

import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.order.domain.OrderPetAcquisition;
import com.xzz.order.dto.OrderDto;
import com.xzz.order.query.OrderPetAcquisitionQuery;
import com.xzz.order.service.impl.OrderPetAcquisitionServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/orderPetAcquisition")
@Api(value = "订单的API",description="订单相关的CRUD功能")//接口文档的注解
public class OrderPetAcquisitionController {

    @Resource
    public OrderPetAcquisitionServiceImpl orderPetAcquisitionService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public OrderPetAcquisition findOne(@PathVariable("id") Long id){
        return orderPetAcquisitionService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<OrderPetAcquisition> findAll(){
        return orderPetAcquisitionService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            orderPetAcquisitionService.del(id);
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
            orderPetAcquisitionService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody OrderPetAcquisition orderPetAcquisition){
        try {
            if(orderPetAcquisition.getId()==null){
                orderPetAcquisitionService.add(orderPetAcquisition);
            }else{
                orderPetAcquisitionService.update(orderPetAcquisition);
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
    public PageList<OrderPetAcquisition> queryPage(@RequestBody OrderPetAcquisitionQuery query, HttpServletRequest request) {
        return orderPetAcquisitionService.queryPage(query,request);
    }

    /**
     * 确认订单
     */
    @PostMapping("/confirm")
    public JsonResult confirm(@RequestBody OrderDto orderDto){
        try {
            orderPetAcquisitionService.confirm(orderDto);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("确认订单失败");
        }
    }

}
