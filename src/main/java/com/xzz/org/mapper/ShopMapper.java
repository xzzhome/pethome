package com.xzz.org.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.org.domain.Department;
import com.xzz.org.domain.Shop;
import com.xzz.org.dto.ShopDto;

import java.util.List;


public interface ShopMapper extends BaseMapper<Shop> {

    Shop loadByNameAndAddress(Shop shop);

    List<ShopDto> getCountByState();
}