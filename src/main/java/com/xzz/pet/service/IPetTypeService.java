package com.xzz.pet.service;

import com.xzz.basic.service.IBaseService;
import com.xzz.pet.domain.Pet;
import com.xzz.pet.domain.PetType;

import java.util.List;

public interface IPetTypeService extends IBaseService<PetType>  {


    List<PetType> typeTree();
}