package com.xzz.basic.service;

import com.xzz.basic.query.BaseQuery;
import com.xzz.basic.query.PageList;
import java.util.List;

public interface IBaseService<T> {

    void add(T t);

    void del(Long id);

    void update(T t);

    T findById(Long id);

    List<T> findAll();

    //分页查询，之后传给页面的    数量和    数据
    PageList<T> queryPage(BaseQuery query);

    void patchDelete(List<Long> ids);
}