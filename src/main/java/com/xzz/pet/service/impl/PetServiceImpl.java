package com.xzz.pet.service.impl;


import com.xzz.basic.service.impl.BaiduAiAuditServiceImpl;
import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.basic.util.JsonResult;
import com.xzz.org.domain.Employee;
import com.xzz.org.mapper.EmployeeMapper;
import com.xzz.pet.domain.Pet;
import com.xzz.pet.domain.PetDetail;
import com.xzz.pet.domain.PetLog;
import com.xzz.pet.mapper.PetDetailMapper;
import com.xzz.pet.mapper.PetLogMapper;
import com.xzz.pet.mapper.PetMapper;
import com.xzz.pet.service.IPetService;
import com.xzz.user.domain.Logininfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.security.auth.login.LoginContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PetServiceImpl extends BaseServiceImpl<Pet> implements IPetService {
    @Autowired
    private PetDetailMapper petDetailMapper;
    @Autowired
    private PetMapper petMapper;
    @Autowired
    private BaiduAiAuditServiceImpl baiduAiAuditService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private PetLogMapper petLogMapper;

    @Transactional
    @Override
    public void add(Pet pet) {
        super.add(pet);
        //获取宠物详情
        PetDetail petDetail = pet.getPetDetail();
        if(petDetail!=null){
            petDetail.setPet_id(pet.getId());
            //添加宠物详情
            petDetailMapper.save(petDetail);
        }
    }

    @Override
    public void update(Pet pet) {
        super.update(pet);
        PetDetail petDetail = pet.getPetDetail();
        if(petDetail!=null){
            //之前在分页查询已经将id查询出来了 - 前端row中有
            petDetail.setPet_id(pet.getId());
            petDetailMapper.update(petDetail);
        }
    }

    @Transactional //Spring5中父类方法上加了@Transactional，子类重写方法也是有效的
    @Override
    public void del(Long id) {
        super.del(id);
        petDetailMapper.delPetDetailByPetId(id);
    }

    @Override
    public JsonResult onsale(List<Long> ids, HttpServletRequest request) {
        //上架 -不能用批量操作，有上架审核
        for (Long id : ids) {
            //上架自动审核文本
            Pet pet = petMapper.loadById(id);
            String auditText = pet.getName();
            Boolean textBoo = baiduAiAuditService.textAudit(auditText);
            //审核图片：多张图片resources
            String petResources = pet.getResources();
            Boolean imageBoo = true;
            if(!StringUtils.isEmpty(petResources)){
                imageBoo = baiduAiAuditService.imageAudit(petResources);
            }

            //获取审核人
            //Logininfo logininfo = (Logininfo) LoginContext.getLoginUser(request);
            //Employee employee = employeeMapper.loadByLogininfoId(logininfo.getId());

            //审核通过
            if (textBoo && imageBoo){
                //数据库操作
                Map<String,Object> params = new HashMap<>();
                params.put("id",id);
                params.put("onsaletime",new Date());
                petMapper.onsale(params);
                //审核成功-note

                //记录审核日志
                PetLog auditLog = new PetLog();
                auditLog.setState(1);
                auditLog.setPet_id(id);
                //auditLog.setAudit_id(employee.getId());
                auditLog.setNote("审核成功！");
                petLogMapper.save(auditLog);
            }else{
                //审核失败-note-当前是下架状态，如果审核失败！
                //记录审核日志
                PetLog auditLog = new PetLog();
                auditLog.setState(0);
                auditLog.setPet_id(id);
                //auditLog.setAudit_id(employee.getId());
                auditLog.setNote("审核失败,宠物名称或图片不合法!!!");
                petLogMapper.save(auditLog);
                //审核失败，上架失败
                return JsonResult.me().setMsg("上架失败！审核失败,宠物名称或图片不合法!!!");
            }
        }
        return JsonResult.me();
    }

    @Override
    public JsonResult offsale(List<Long> ids, HttpServletRequest request) {
        //下架：修改state为0，offsaletime为当前时间 ,可以用批量操作
        Map<String,Object> params = new HashMap<>();
        params.put("ids",ids);
        params.put("offsaletime",new Date());
        petMapper.offsale(params);
        return JsonResult.me();
    }
}
