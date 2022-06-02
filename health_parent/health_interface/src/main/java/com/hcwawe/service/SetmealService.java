package com.hcwawe.service;

import com.hcwawe.entity.PageResult;
import com.hcwawe.entity.QueryPageBean;
import com.hcwawe.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    public void add(Setmeal setmeal,Integer[] checkgroupIds);

    public PageResult queryPage(QueryPageBean queryPageBean);

    public Setmeal findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroup(Integer id);

    public void edit(Setmeal setmeal, Integer[] checkgroupIds);

    public void deleteById(Integer id);

    public List<Setmeal> findAll();

    public Setmeal findSetmealById(Integer id);
}
