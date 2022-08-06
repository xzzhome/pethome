package com.xzz.pet.mapper;

import com.xzz.basic.mapper.BaseMapper;
import com.xzz.pet.domain.Pet;
import com.xzz.pet.domain.PetDetail;


public interface PetDetailMapper extends BaseMapper<PetDetail> {


    void delPetDetailByPetId(Long id);
}