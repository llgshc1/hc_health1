package com.hcwawe.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hcwawe.dao.CheckGroupDao;
import com.hcwawe.entity.PageResult;
import com.hcwawe.entity.QueryPageBean;
import com.hcwawe.pojo.CheckGroup;
import com.hcwawe.service.CheckGroupService;
import com.hcwawe.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
//        操作新增检查组t_checkgroup
        checkGroupDao.add(checkGroup);
//        设置检查组 检查项的多对多关系 操作 t_check
        Integer checkGroupId = checkGroup.getId();

//        建立检查组和检查项的多对多关系
        setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckGroup> result = page.getResult();
        return new PageResult(total,result);

    }

    @Override
    public CheckGroup findById(Integer id) {
       return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroup(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroup(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
//        先清理关联的检查项 在重新关联
        checkGroupDao.edit(checkGroup);

        Integer checkGroupId = checkGroup.getId();
        checkGroupDao.deleteAssociation(checkGroupId);
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);

    }

    @Override
    public void delete(Integer id) {
        checkGroupDao.deleteAssociation(id);
        checkGroupDao.deleteById(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        List<CheckGroup> list = checkGroupDao.findAll();
        return list;
    }

    // 抽取方法关联检查组于检查项
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){

        if (checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroupId",checkGroupId);
                map.put("checkitmId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
