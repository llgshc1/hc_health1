package com.hcwawe.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hcwawe.constant.RedisConstant;
import com.hcwawe.dao.SetmealDao;
import com.hcwawe.entity.PageResult;
import com.hcwawe.entity.QueryPageBean;
import com.hcwawe.pojo.Setmeal;
import com.hcwawe.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 体检套餐服务
* */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private JedisPool jedisPool;
//    新增套餐信息 关联中间表
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outPutPath;
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
//        先添加套餐
        setmealDao.add(setmeal);
//        关联套餐与选中的checkgroup
        Integer setmealId = setmeal.getId();
       if(checkgroupIds != null && checkgroupIds.length > 0 ){
           for (Integer checkgroupId : checkgroupIds) {
               setmealDao.setSetmealAndCheckGroup(setmealId,checkgroupId);
           }
       }
//        调用方法 保存到redis
        if(setmeal.getImg()!=null){
            savePic2Redis(setmeal.getImg());
        }
//        当添加套餐之后 重新生成静态页面(套餐列表） 套餐详情
        //generateMobileStaticHtml();
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.queryByCondition(queryString);
        long total = page.getTotal();
        List<Setmeal> list = page.getResult();
        return new PageResult(total,list);
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroup(Integer id) {
        return setmealDao.findCheckItemIdsByCheckGroup(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
//        先编辑setmeal
        setmealDao.updateSetmeal(setmeal);
//        清除与setmeal关联得checkgroup
        Integer setmealId = setmeal.getId();
        setmealDao.deleteBySetmealId(setmealId);
//        新关联setmeal与checkgroup得关系
        if(checkgroupIds !=null && checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.setSetmealAndCheckGroup(setmealId,checkgroupId);
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
//        先清除与之关联的检查组
        setmealDao.deleteBySetmealId(id);
//        删除套餐
        setmealDao.deleteById(id);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findSetmealById(Integer id) {
        return setmealDao.findSetmealById(id);
    }
//    生成当前方法所需的静态页面
    public void generateMobileStaticHtml(){
//        生成静态页面查询数据
        List<Setmeal> list = setmealDao.findAll();
//        套餐列表静态页面
        generateMobileSetmealListHtml(list);
//        套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }
//    生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> list){
        Map map = new HashMap();
//        为模板提供数据，用于生成静态页面
        map.put("setmealList",list);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }
//    生成套餐详情页面（有多个套餐详情）
    public void generateMobileSetmealDetailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map map = new HashMap();
            map.put("setmeal",setmealDao.findSetmealById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }
    }
// 通用的   生成静态页面的方法
    public void generateHtml(String teplateName, String htmlPageName, Map<String,Object> dataMap){
        Writer out = null;
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            Template template = configuration.getTemplate(teplateName);
            out  = new FileWriter(new File(outPutPath+"/"+htmlPageName));
           template.process(dataMap,out);
           out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    //    抽取方法 将已经保存到数据库的img文件保存到redis
    private void savePic2Redis(String pic){

        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }
    
}
