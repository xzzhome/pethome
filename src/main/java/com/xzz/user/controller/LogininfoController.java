package com.xzz.user.controller;

import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.query.PageList;
import com.xzz.basic.util.JsonResult;
import com.xzz.user.domain.Logininfo;
import com.xzz.user.dto.BinderDto;
import com.xzz.user.dto.LoginDto;
import com.xzz.user.query.LogininfoQuery;
import com.xzz.user.service.impl.LogininfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/login")
@Api(value = "用户的API",description="用户相关的CRUD功能")//接口文档的注解
public class LogininfoController {

    @Resource
    public LogininfoServiceImpl logininfoService;

    //查询单个对象
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询" )
    public Logininfo findOne(@PathVariable("id") Long id){
        return logininfoService.findById(id);
    }

    //查询所有对象
    @GetMapping
    @ApiOperation(value = "查询所有对象" )
    public List<Logininfo> findAll(){
        return logininfoService.findAll();
    }

    //删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "通过ID删除" )
    public JsonResult delete(@PathVariable("id") Long id){
        try {
            logininfoService.del(id);
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
            logininfoService.patchDelete(ids);
            return JsonResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("批量删除失败");
        }
    }

    //添加或修改
    @PutMapping
    @ApiOperation(value = "添加或修改" )
    public JsonResult addOrUpdate(@RequestBody Logininfo logininfo){
        try {
            if(logininfo.getId()==null){
                logininfoService.add(logininfo);
            }else{
                logininfoService.update(logininfo);
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
    public PageList<Logininfo> queryPage(@RequestBody LogininfoQuery logininfoQuery){
        return logininfoService.queryPage(logininfoQuery);
    }
    
    //登录
    @PostMapping("/account")
    public JsonResult accountLogin(@RequestBody LoginDto loginDto){
        try {
            //登录成功要返回token登录信息 - 代替session
            return logininfoService.accountLogin(loginDto);
        } catch (BusinessException e){
            e.printStackTrace();
            return JsonResult.me().setMsg("登录失败，"+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("系统繁忙，请稍后重试!!!");
        }
    }

    /**
     * 微信登录接口
     * @param code
     * @return
     */
    @GetMapping("/wechat/{code}")
    public JsonResult wechatLogin(@PathVariable("code") String code){
        try {
            return logininfoService.wechatLogin(code);
        } catch (BusinessException e) {
            e.printStackTrace();
            return JsonResult.me().setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("系统异常，请稍后重试!!!");
        }
    }

    /**
     * 微信绑定接口
     * @param binderDto
     * @return
     */
    @PostMapping("/wechat/binder")
    public JsonResult wechatBinder(@RequestBody BinderDto binderDto){
        try {
            return logininfoService.wechatBinder(binderDto);
        } catch (BusinessException e) {
            e.printStackTrace();
            return JsonResult.me().setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.me().setMsg("系统异常，请稍后重试!!!");
        }
    }
}


