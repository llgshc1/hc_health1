package com.hcwawe.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hcwawe.dao.CheckItemDao;
import com.hcwawe.entity.PageResult;
import com.hcwawe.entity.QueryPageBean;
import com.hcwawe.pojo.CheckItem;
import com.hcwawe.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
* 检查项服务
* */
//添加事务接口之后 必须指定实现的是哪个接口
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
//检查项 分页查询
    @Override
    public PageResult pageQeury(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
//        完成分页查询 基于mybatis框架提供的分页助手
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);

        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        return new PageResult(total,rows);
    }

    @Override
    public void deleteById(Integer id) {
//        判断检查项是否已经关联到检查组 如果关联 能删除
        long count = checkItemDao.findCountByCheckItemId(id);
        if(count>0){
            new RuntimeException();
        }
            checkItemDao.deleteById(id);


    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
