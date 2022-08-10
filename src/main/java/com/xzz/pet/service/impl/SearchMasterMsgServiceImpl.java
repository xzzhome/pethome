package com.xzz.pet.service.impl;


import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.query.PageList;
import com.xzz.basic.service.IBaiduAiAuditService;
import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.basic.util.CodeGenerateUtils;
import com.xzz.basic.util.DistanceUtils;
import com.xzz.basic.util.LoginContext;
import com.xzz.basic.util.Point;
import com.xzz.order.domain.OrderPetAcquisition;
import com.xzz.order.mapper.OrderPetAcquisitionMapper;
import com.xzz.order.service.IOrderPetAcquisitionService;
import com.xzz.org.domain.Employee;
import com.xzz.org.domain.Shop;
import com.xzz.org.mapper.EmployeeMapper;
import com.xzz.org.mapper.ShopMapper;
import com.xzz.pet.domain.Pet;
import com.xzz.pet.domain.PetDetail;
import com.xzz.pet.domain.SearchMasterMsg;
import com.xzz.pet.domain.SearchMasterMsgLog;
import com.xzz.pet.dto.AcceptDto;
import com.xzz.pet.mapper.PetMapper;
import com.xzz.pet.mapper.SearchMasterMsgLogMapper;
import com.xzz.pet.mapper.SearchMasterMsgMapper;
import com.xzz.pet.query.SearchMasterMsgQuery;
import com.xzz.pet.service.IPetDetailService;
import com.xzz.pet.service.ISearchMasterMsgService;
import com.xzz.user.domain.Logininfo;
import com.xzz.user.domain.User;
import com.xzz.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Service
public class SearchMasterMsgServiceImpl extends BaseServiceImpl<SearchMasterMsg> implements ISearchMasterMsgService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private SearchMasterMsgMapper searchMasterMsgMapper;
    @Autowired
    private SearchMasterMsgLogMapper searchMasterMsgLogMapper;
    @Autowired
    private IBaiduAiAuditService baiduAiAuditService;
    @Autowired
    private OrderPetAcquisitionMapper orderPetAcquisitionMapper;
    @Autowired
    private PetMapper petMapper;

    @Override
    public void publish(SearchMasterMsg searchMasterMsg, Long id) {
        //设置登录人的用户信息
        User user = userMapper.loadByLogininfoId(id);
        searchMasterMsg.setUser_id(user.getId());
        //添加进入数据库
        searchMasterMsgMapper.save(searchMasterMsg);

        //2.智能审核
        Boolean textBoo = baiduAiAuditService.textAudit(searchMasterMsg.getTitle() + searchMasterMsg.getName() + searchMasterMsg.getCoatColor() + searchMasterMsg.getAddress());
        Boolean imageBoo = baiduAiAuditService.imageAudit(searchMasterMsg.getResources());

        if(textBoo && imageBoo){
            //设置状态和日志
            searchMasterMsg.setState(1);
            searchMasterMsg.setNote("审核成功");
            //设置店铺信息
            Point point = DistanceUtils.getPoint(searchMasterMsg.getAddress());
            Shop nearestShop = DistanceUtils.getNearestShop(point, shopMapper.loadAll());
            searchMasterMsg.setShop_id(nearestShop.getId());
            //修改数据库
            searchMasterMsgMapper.update(searchMasterMsg);
            //添加审核日志
            SearchMasterMsgLog log = new SearchMasterMsgLog();
            log.setMsg_id(searchMasterMsg.getId());
            //audit_id - 自动审核，没有添加审核人
            log.setState(1);//审核通过
            log.setNote("审核成功，happy");
            searchMasterMsgLogMapper.save(log);

        }else {
            //修改状态和日志
            searchMasterMsg.setState(0);
            searchMasterMsg.setNote("审核失败，内容非法");
            searchMasterMsgMapper.update(searchMasterMsg);
            //审核失败：修改寻主消息状态0 + 添加审核日志
            SearchMasterMsgLog log = new SearchMasterMsgLog();
            log.setMsg_id(searchMasterMsg.getId());
            //audit_id - 自动审核，没有添加审核人
            log.setState(0);//驳回
            log.setNote("审核失败，内容非法");
            searchMasterMsgLogMapper.save(log);
            throw new BusinessException("审核失败，内容非法");
        }
    }

    @Override
    public PageList<SearchMasterMsg> toHandle(SearchMasterMsgQuery query, HttpServletRequest request) {
        query.setState(1);
        Logininfo logininfo = LoginContext.getLogininfo(request);
        Employee employee = employeeMapper.loadByLogininfoId(logininfo.getId());
        if(employee.getShop_id() !=null ){
            query.setShop_id(employee.getShop_id());
        }
        return super.queryPage(query);
    }

    @Override
    public PageList<SearchMasterMsg> userSearchMasterMsg(SearchMasterMsgQuery query, HttpServletRequest request) {
        Logininfo logininfo = LoginContext.getLogininfo(request);
        User user = userMapper.loadByLogininfoId(logininfo.getId());
        query.setUser_id(user.getId());
        return super.queryPage(query);
    }

    @Override
    public void reject(Long id) {
        searchMasterMsgMapper.reject(id);
    }

    @Override
    public void accept(AcceptDto dto) {
        Long msgId = dto.getMsgId();
        Long handlerId = dto.getHandler();
        String note = dto.getNote();
        //获取寻主消息
        SearchMasterMsg searchMasterMsg = searchMasterMsgMapper.loadById(msgId);
        //获取处理订单的员工信息
        Employee handler = employeeMapper.loadById(handlerId);

        //1.校验改寻主消息是否已经在处理，报错
        OrderPetAcquisition orderTmp = orderPetAcquisitionMapper.loadByMsgId(msgId);
        if (orderTmp!=null){
            throw new BusinessException("已经被接单，正在处理中！");
        }

        //2 创建寻主消息所对应宠物信息
        Pet pet = searchMasterMsg2Pet(searchMasterMsg);
        petMapper.save(pet);

        //3.创建对应的订单
        OrderPetAcquisition order = buildPetAcquisitionOrder(searchMasterMsg, handler, pet);
        orderPetAcquisitionMapper.save(order);

        //4 发送消息给对应的工作人员
        //System.out.println(handler.getEmail()+"-->邮件通知：您有新的"+order.getOrderSn()+"订单要处理！");
        //System.out.println(handler.getPhone()+"-->短信通知：您有新的"+order.getOrderSn()+"订单要处理！");
    }

    private OrderPetAcquisition buildPetAcquisitionOrder(SearchMasterMsg searchMasterMsg, Employee handler, Pet pet) {

        OrderPetAcquisition order = new OrderPetAcquisition();
        //随机生成订单编号
        order.setOrderSn(CodeGenerateUtils.generateOrderSn(handler.getId()));
        //摘要
        order.setDigest(searchMasterMsg.getName()+"的收购订单");
        //最后收购是当前时间+2两天
        order.setLastcomfirmtime(new Date(System.currentTimeMillis()+1000*60*60*24*2));

        order.setState(0);
        order.setPrice(searchMasterMsg.getPrice());
        //order.setPaytype(); //现金支付
        order.setPet_id(pet.getId());
        order.setUser_id(searchMasterMsg.getUser_id());
        order.setShop_id(pet.getShop_id());
        order.setAddress(searchMasterMsg.getAddress());
        order.setEmp_id(handler.getId());
        order.setSearch_master_msg_id(searchMasterMsg.getId());
        return order;
    }

    private Pet searchMasterMsg2Pet(SearchMasterMsg searchMasterMsg) {
        Pet pet = new Pet();
        pet.setName(searchMasterMsg.getName());
        pet.setResources(searchMasterMsg.getResources());
        pet.setType_id(searchMasterMsg.getPetType());
        //就要获取处理人的店铺id
        pet.setShop_id(searchMasterMsg.getShop_id());
        pet.setUser_id(searchMasterMsg.getUser_id());
        pet.setSearch_master_msg_id(searchMasterMsg.getId());
        //用户发布时需要的价格 收购回来就是成本价
        pet.setCostprice(searchMasterMsg.getPrice());
        return pet;
    }
}
