package com.hcwawe.service;

import com.hcwawe.entity.PageResult;
import com.hcwawe.entity.QueryPageBean;
import com.hcwawe.pojo.CheckItem;

import java.util.List;

//服务接口
public interface CheckItemService {
    public void add(CheckItem checkItem);
    public PageResult pageQeury(QueryPageBean queryPageBean);
    public void deleteById(Integer id);
    public void edit(CheckItem checkItem);
    public List<CheckItem> findAll();
}
