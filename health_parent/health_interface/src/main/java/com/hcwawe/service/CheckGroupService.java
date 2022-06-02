package com.hcwawe.service;

import com.hcwawe.entity.PageResult;
import com.hcwawe.entity.QueryPageBean;
import com.hcwawe.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    public void add(CheckGroup checkGroup,Integer[] checkitemIds);
    public PageResult pageQuery(QueryPageBean queryPageBean);
    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroup(Integer id);

    public void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    public void delete(Integer id);

    public List<CheckGroup> findAll();
}
