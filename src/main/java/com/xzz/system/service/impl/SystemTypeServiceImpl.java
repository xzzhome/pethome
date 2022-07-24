package com.xzz.system.service.impl;

import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.system.domain.SystemDetail;
import com.xzz.system.domain.SystemType;
import com.xzz.system.mapper.SystemDetailMapper;
import com.xzz.system.mapper.SystemTypeMapper;
import com.xzz.system.mapper.SystemTypeMapper;
import com.xzz.system.service.ISystemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemTypeServiceImpl extends BaseServiceImpl<SystemType> implements ISystemTypeService {
    
    @Autowired
    private SystemTypeMapper mapper;

    @Autowired
    private SystemDetailMapper detailMapper;

    @Autowired
    private SystemType systemType;

    /*@Override
    public List<SystemType> deptTree() {
        //查找所有部门数据
        List<SystemType> all = mapper.loadAll();
        List<SystemDetail> details = detailMapper.loadAll();

        //将全部部门的id作为K，数据作为V，存入map
        Map<Long,SystemDetail> map  = new HashMap<>();
        for (SystemDetail systemDetail : details) {
            map.put(systemDetail.getTypes_id(),systemDetail);
        }

        //前端需要得到的集合数据 - 部门树（级联树的本质是集合，树里面只存树干，也就是顶级部门）
        List<SystemType> deptTree = new ArrayList<>();

        for (SystemType systemType:all){
            if(systemType.getId()!=null){//顶级部门
                //1.想办法找到当前部门的父部门
                Long types_id = systemType.getId();
                //2.通过父部门id，与map中的K匹配，匹配成功即，找到对应的父部门，读取父部门的全部数据
                SystemDetail detail = map.get(types_id);
                //3.将自己装到父部门的children字段中
                systemType.getChildren().add(detail);
                deptTree.add(systemType);
            }else{//不是顶级部门

            }
        }
        return deptTree;
    }*/
}
