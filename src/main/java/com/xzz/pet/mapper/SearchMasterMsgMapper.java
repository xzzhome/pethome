package com.xzz.pet.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.pet.domain.PetType;
import com.xzz.pet.domain.SearchMasterMsg;


public interface SearchMasterMsgMapper extends BaseMapper<SearchMasterMsg> {

    void reject(Long id);
}