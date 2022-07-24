package com.xzz.org.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.org.domain.Department;
import com.xzz.org.domain.Shop;


public interface ShopMapper extends BaseMapper<Shop> {


    Shop loadByNameAndAddress(Shop shop);
}