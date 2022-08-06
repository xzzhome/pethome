package com.xzz.pet.service.impl;


import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.pet.domain.Pet;
import com.xzz.pet.domain.PetType;
import com.xzz.pet.mapper.PetTypeMapper;
import com.xzz.pet.service.IPetService;
import com.xzz.pet.service.IPetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PetTypeServiceImpl extends BaseServiceImpl<PetType> implements IPetTypeService {
    @Autowired
    private PetTypeMapper petTypeMapper;

    @Override
    public List<PetType> typeTree() {
        List<PetType> allTypes = petTypeMapper.loadAll();

        //避免双重for循环
        Map<Long, PetType> map = new HashMap<>();
        //将所有部门装到map中
        for (PetType petType: allTypes) {
            map.put(petType.getId(),petType);
        }

        List<PetType> typeTree = new ArrayList<>();
        for (PetType petType: allTypes) {
            //如果是顶级部门 - 就添加到部门数
            if(petType.getPid() == null){
                typeTree.add(petType);
            }else{//有父级部门
                //获取当前部门的父部门id
                Long parent_id = petType.getPid();
                //根据父部门id获取父部门
                PetType partentType = map.get(parent_id);
                //将自己添加到父部门的集合列表中
                partentType.getChildren().add(petType);
            }
        }
        return typeTree;
    }
}
