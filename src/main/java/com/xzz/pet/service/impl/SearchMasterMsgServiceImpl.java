package com.xzz.pet.service.impl;


import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.service.IBaiduAiAuditService;
import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.basic.util.DistanceUtils;
import com.xzz.basic.util.Point;
import com.xzz.org.domain.Shop;
import com.xzz.org.mapper.ShopMapper;
import com.xzz.pet.domain.PetDetail;
import com.xzz.pet.domain.SearchMasterMsg;
import com.xzz.pet.domain.SearchMasterMsgLog;
import com.xzz.pet.mapper.SearchMasterMsgLogMapper;
import com.xzz.pet.mapper.SearchMasterMsgMapper;
import com.xzz.pet.service.IPetDetailService;
import com.xzz.pet.service.ISearchMasterMsgService;
import com.xzz.user.domain.User;
import com.xzz.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service
public class SearchMasterMsgServiceImpl extends BaseServiceImpl<SearchMasterMsg> implements ISearchMasterMsgService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private SearchMasterMsgMapper searchMasterMsgMapper;
    @Autowired
    private SearchMasterMsgLogMapper searchMasterMsgLogMapper;
    @Autowired
    private IBaiduAiAuditService baiduAiAuditService;

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
}
