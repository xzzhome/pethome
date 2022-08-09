package com.xzz.pet.controller;

import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.basic.util.LoginContext;
import com.xzz.pet.domain.SearchMasterMsg;
import com.xzz.pet.query.SearchMasterMsgQuery;
import com.xzz.pet.service.impl.SearchMasterMsgServiceImpl;
import com.xzz.user.domain.Logininfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/searchMasterMsg")
@Api(value = "寻主的API",description="寻主相关的CRUD功能")//接口文档的注解
public class SearchMasterMsgController {

    @Resource
    public SearchMasterMsgServiceImpl searchMasterMsgService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public SearchMasterMsg findOne(@PathVariable("id") Long id){
        return searchMasterMsgService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<SearchMasterMsg> findAll(){
        return searchMasterMsgService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            searchMasterMsgService.del(id);
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
            searchMasterMsgService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody SearchMasterMsg searchMasterMsg){
        try {
            if(searchMasterMsg.getId()==null){
                searchMasterMsgService.add(searchMasterMsg);
            }else{
                searchMasterMsgService.update(searchMasterMsg);
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
    public PageList<SearchMasterMsg> queryPage(@RequestBody SearchMasterMsgQuery searchMasterMsgQuery){
        return searchMasterMsgService.queryPage(searchMasterMsgQuery);
    }

    //发布寻主消息
    @PostMapping("/publish")
    @ApiOperation(value = "发布寻主消息",notes = "")
    public JsonResult publish(@RequestBody SearchMasterMsg searchMasterMsg, HttpServletRequest request){
        try {
            Logininfo logininfo = LoginContext.getLogininfo(request);
            searchMasterMsgService.publish(searchMasterMsg,logininfo.getId());
            return JsonResult.me();
        } catch (BusinessException e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("发布失败!" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("系统错误，稍后重试!!!");
        }
    }

}
