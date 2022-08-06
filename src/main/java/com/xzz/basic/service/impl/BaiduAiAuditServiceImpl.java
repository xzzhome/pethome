package com.xzz.basic.service.impl;

import com.xzz.basic.service.IBaiduAiAuditService;
import org.springframework.stereotype.Service;

@Service
public class BaiduAiAuditServiceImpl implements IBaiduAiAuditService {

    @Override
    public Boolean textAudit(String auditText) {
        //@TODO - 自主研发 - 工具类BaiduAuditUtils
        return true;
    }

    /*
     *  注意:resources是这样的格式：
     *  /group1/M00/00/02/CgAIC2KevEeAX2T4AAEUusLjqqk161.png,/group1/M00/00/02/CgAIC2KevEeAX2T4AAEUusLjqqk162.png
     *  需要判断是否为空，只有一张怎么处理，有多张怎么处理
     *  需要统一加前缀：http://123.207.27.208
     */
    @Override
    public Boolean imageAudit(String resources) {
        //@TODO - 自主研发 - 工具类BaiduAuditUtils
        return true;
    }
}