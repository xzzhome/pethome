package com.xzz.system.service.impl;

import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.system.domain.Menu;
import com.xzz.system.mapper.MenuMapper;
import com.xzz.system.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
// 父类有事务，该注解有继承性
// @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> menuTree() {
        List<Menu> allMenus = menuMapper.loadAll();

        //避免双重for循环
        Map<Long,Menu> map = new HashMap<>();
        //将所有部门装到map中
        for (Menu menu: allMenus) {
            map.put(menu.getId(),menu);
        }

        List<Menu> menuTree = new ArrayList<>();
        for (Menu menu: allMenus) {
            //如果是顶级部门 - 就添加到部门数
            if(menu.getParent_id() == null){
                menuTree.add(menu);
            }else{//有父级部门
                //获取当前部门的父部门id
                Long parent_id = menu.getParent_id();
                //根据父部门id获取父部门
                Menu m = map.get(parent_id);
                //将自己添加到父部门的集合列表中
                m.getChildren().add(menu);
            }
        }
        return menuTree;
    }

}

