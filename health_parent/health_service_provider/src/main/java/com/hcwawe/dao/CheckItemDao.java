package com.hcwawe.dao;

import com.github.pagehelper.Page;
import com.hcwawe.pojo.CheckGroup;
import com.hcwawe.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    public void add(CheckItem checkItem);
    public Page<com.hcwawe.pojo.CheckItem> selectByCondition(String queryString);
    public long findCountByCheckItemId(Integer id);
    public void deleteById(Integer id);
    public void edit(CheckItem checkItem);
    public List<CheckItem> findAll();
    public CheckGroup findById(Integer id);
}
