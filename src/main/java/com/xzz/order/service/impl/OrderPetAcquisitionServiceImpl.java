package com.xzz.order.service.impl;


import com.xzz.basic.query.PageList;
import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.basic.util.LoginContext;
import com.xzz.order.domain.OrderPetAcquisition;
import com.xzz.order.dto.OrderDto;
import com.xzz.order.mapper.OrderPetAcquisitionMapper;
import com.xzz.order.query.OrderPetAcquisitionQuery;
import com.xzz.order.service.IOrderPetAcquisitionService;
import com.xzz.org.domain.Employee;
import com.xzz.org.domain.Shop;
import com.xzz.org.mapper.EmployeeMapper;
import com.xzz.org.mapper.ShopMapper;
import com.xzz.pet.domain.Pet;
import com.xzz.pet.domain.SearchMasterMsg;
import com.xzz.pet.mapper.PetMapper;
import com.xzz.pet.mapper.SearchMasterMsgMapper;
import com.xzz.user.domain.Logininfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


@Service
public class OrderPetAcquisitionServiceImpl extends BaseServiceImpl<OrderPetAcquisition> implements IOrderPetAcquisitionService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private OrderPetAcquisitionMapper orderPetAcquisitionMapper;
    @Autowired
    private SearchMasterMsgMapper searchMasterMsgMapper;
    @Autowired
    private PetMapper petMapper;

    @Override
    public PageList<OrderPetAcquisition> queryPage(OrderPetAcquisitionQuery query, HttpServletRequest request) {
        Logininfo logininfo = LoginContext.getLogininfo(request);
        Employee employee = employeeMapper.loadByLogininfoId(logininfo.getId());
        // 判断登录员工是那种角色
        //1.employee中shop_id为null，平台管理员
        Long shopId = employee.getShop_id();
        //2.店铺人员
        if (shopId != null) {
            Shop shop = shopMapper.loadById(shopId);
            if (shop.getAdmin_id() != employee.getId()) {
                //3.否则就为店员
                query.setEmpId(employee.getId());
            }
            //4.employee中shop_id不为null，通过shop_id查询店铺得到admin_id如果为当前登录管理员的id，就为店铺管理员
            query.setShopId(shopId);
        }
        //5.平台管理员
        return super.queryPage(query);
    }

    @Override
    public void confirm(OrderDto orderDto) {
        Long orderId = orderDto.getId();
        Integer payType = orderDto.getPayType();
        BigDecimal money = orderDto.getMoney();

        //1.修改订单：state = 1，money，payType
        OrderPetAcquisition order = orderPetAcquisitionMapper.loadById(orderId);
        order.setState(1);
        order.setPrice(money);
        order.setPaytype(payType);
        orderPetAcquisitionMapper.update(order);

        //2.修改寻主消息：state=2，note
        SearchMasterMsg msg = searchMasterMsgMapper.loadById(order.getSearch_master_msg_id());
        msg.setState(2);
        msg.setNote("处理完成，交易成功");
        searchMasterMsgMapper.update(msg);

        //3.宠物 修改一个成本价 = money
        Pet pet = petMapper.loadById(order.getPet_id());
        pet.setCostprice(money);
        petMapper.update(pet);
    }
}
