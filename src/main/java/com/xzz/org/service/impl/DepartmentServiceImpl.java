package com.xzz.org.service.impl;

import com.xzz.basic.query.PageList;
import com.xzz.org.domain.Department;
import com.xzz.org.mapper.DepartmentMapper;
import com.xzz.org.query.DepartmentQuery;
import com.xzz.org.service.IDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class DepartmentServiceImpl implements IDepartmentService {

    @Resource
    private DepartmentMapper mapper;

    @Transactional
    @Override
    public void add(Department department) {
        //保存dirpath之外的数据，以及得到SQL中返回的自增长的id，才能用于更新修改
        mapper.save(department);
        //更新到数据库，直接用service层的修改方法
        update(department);
    }

    @Transactional
    @Override
    public void del(Long id) {
        mapper.remove(id);
    }

    @Transactional
    @Override
    public void update(Department department) {
        //如果没有上级部门
        if(department.getParent_id()==null){
            //只添加自己的id - 自增长id
            department.setDirPath("/"+department.getId());
        }else{
            //有上级部门，获取上级部门id
            Long parent_id = department.getParent_id();
            //获取上级部门的dirPath
            String parent_dirPath = mapper.loadById(parent_id).getDirPath();
            //组装自己的id
            department.setDirPath(parent_dirPath+"/"+department.getId());
        }
        mapper.update(department);
    }

    @Override
    public Department findById(Long id) {
        return mapper.loadById(id);
    }

    @Override
    public List<Department> findAll() {
        return mapper.loadAll();
    }

    @Override
    public PageList<Department> queryPage(DepartmentQuery query) {
        Integer totals = mapper.queryCount(query);
        if (totals==null || totals==0){
            return  new PageList<>();
        }
        List<Department> data = mapper.queryData(query);
        return new PageList<>(totals,data);
    }

    @Transactional
    @Override
    public void patchDelete(List<Long> ids) {
        mapper.patchDelete(ids);
    }

    @Override
    public List<Department> deptTree() {
        //查找所有部门数据
        List<Department> all = mapper.loadAll();

        //将全部部门的id作为K，数据作为V，存入map
        Map<Long,Department> map  = new HashMap<>();
        for (Department department : all) {
            map.put(department.getId(),department);
        }

        //前端需要得到的集合数据 - 部门树（级联树的本质是集合，树里面只存树干，也就是顶级部门）
        List<Department> deptTree = new ArrayList<>();

        for (Department department:all){
            if(department.getParent_id() == null){//顶级部门
                deptTree.add(department);
            }else{//不是顶级部门
                //1.想办法找到当前部门的父部门
                Long parent_id = department.getParent_id();
                //2.通过父部门id，与map中的K匹配，匹配成功即，找到对应的父部门，读取父部门的全部数据
                Department parentDepartment = map.get(parent_id);
                //3.将自己装到父部门的children字段中
                parentDepartment.getChildren().add(department);
            }
        }
        return deptTree;
    }
}
