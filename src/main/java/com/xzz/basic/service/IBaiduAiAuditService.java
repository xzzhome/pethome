package com.xzz.basic.service;

//百度人工智能审核服务：自主研发
public interface IBaiduAiAuditService {
     //文本审核
     Boolean textAudit(String text);
     
    /**
     * 对图片进行审核：fastDfs上的图片
     * @param resources 资源，多张图片通过会用“，”分隔
     * @return
     */
     Boolean imageAudit(String resources);
}