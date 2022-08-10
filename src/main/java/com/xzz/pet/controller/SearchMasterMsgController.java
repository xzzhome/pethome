package com.xzz.pet.controller;

import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.basic.util.LoginContext;
import com.xzz.pet.domain.SearchMasterMsg;
import com.xzz.pet.dto.AcceptDto;
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

    // 待审核或驳回：查询待审核或驳回的寻主消息 - 平台管理员才能查询 - 可以通过按钮权限限制，这里不做判断
    @PostMapping("/toAudit")
    public PageList<SearchMasterMsg> toAudit(@RequestBody SearchMasterMsgQuery query){
        query.setState(0);
        return searchMasterMsgService.queryPage(query);
    }

    // 已完成：查询已完成的寻主消息 - 平台管理员才能查询 - 可以通过按钮权限限制，这里不做判断
    @PostMapping("/finish")
    public PageList<SearchMasterMsg> finish(@RequestBody SearchMasterMsgQuery query){
        query.setState(2);
        return searchMasterMsgService.queryPage(query);
    }

    // 待处理：查询审核通过并已经分配了店铺的寻主消息 - state=1，ShopId不为null
    @PostMapping("/toHandle")
    public PageList<SearchMasterMsg> toHandle(@RequestBody SearchMasterMsgQuery query,HttpServletRequest request){
        return searchMasterMsgService.toHandle(query,request);
    }

    // 用户寻主列表：用于登录之后再个人中心查询自己所有状态的寻主消息
    @PostMapping("/user")
    public PageList<SearchMasterMsg> userSearchMasterMsg(@RequestBody SearchMasterMsgQuery query,HttpServletRequest request){
        return searchMasterMsgService.userSearchMasterMsg(query,request);
    }

    // 拒单
    @GetMapping("/reject/{id}")
    public JsonResult reject(@PathVariable("id")Long id){
        try {
            searchMasterMsgService.reject(id);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("拒单失败！");
        }
    }

    // 接单
    @PostMapping("/accept")
    public JsonResult accept(@RequestBody AcceptDto dto){
        try {
            searchMasterMsgService.accept(dto);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("接单失败！"+e.getMessage());
        }
    }

}
