package com.hcwawe.dao;

import com.github.pagehelper.Page;
import com.hcwawe.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealDao {
    public void add(Setmeal setmeal);
    public void setSetmealAndCheckGroup(@Param("setmealId") Integer setmealId,@Param("checkGroupId") Integer checkGroupId );

    public Page<Setmeal> queryByCondition(String queryString);

    public Setmeal findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroup(Integer id);

    public void updateSetmeal(Setmeal setmeal);

    public void deleteBySetmealId(Integer setmealId);

    public void deleteById(Integer id);

    public List<Setmeal> findAll();

    public Setmeal findSetmealById(Integer id);
}
