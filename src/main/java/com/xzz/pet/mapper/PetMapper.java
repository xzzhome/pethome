package com.xzz.pet.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.org.domain.Shop;
import com.xzz.org.dto.ShopDto;
import com.xzz.pet.domain.Pet;

import java.util.List;
import java.util.Map;


public interface PetMapper extends BaseMapper<Pet> {


    void offsale(Map<String, Object> params);

    void onsale(Map<String, Object> params);
}