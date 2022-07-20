package com.xzz.basic.mapper;

import com.xzz.basic.query.BaseQuery;
import java.util.List;

public interface BaseMapper<T> {

    void save(T t);

    void remove(Long id);

    void update(T t);

    T loadById(Long id);

    List<T> loadAll();

    //分页：查询总数量
    Integer queryCount(BaseQuery query);

    //分页：查询当前页数据
    List<T> queryData(BaseQuery query);

    void patchDelete(List<Long> ids);
}