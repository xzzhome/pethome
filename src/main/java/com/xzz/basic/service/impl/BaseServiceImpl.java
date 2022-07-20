package com.xzz.basic.service.impl;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.basic.query.BaseQuery;
import com.xzz.basic.query.PageList;
import com.xzz.basic.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class BaseServiceImpl<T> implements IBaseService<T> {

    @Autowired
    private BaseMapper<T> baseMapper;

    @Transactional
    @Override
    public void add(T t) {
        baseMapper.save(t);
    }

    @Transactional
    @Override
    public void del(Long id) {
        baseMapper.remove(id);
    }

    @Transactional
    @Override
    public void update(T t) {
        baseMapper.update(t);
    }

    @Override
    public T findById(Long id) {
        return baseMapper.loadById(id);
    }

    @Override
    public List<T> findAll() {
        return baseMapper.loadAll();
    }

    @Override
    public PageList<T> queryPage(BaseQuery query) {
        Integer totals = baseMapper.queryCount(query);
        if (totals==null || totals==0){
            return  new PageList<T>();
        }
        List<T> data = baseMapper.queryData(query);
        return new PageList<T>(totals,data);
    }

    @Transactional
    @Override
    public void patchDelete(List<Long> ids) {
        baseMapper.patchDelete(ids);
    }
}
