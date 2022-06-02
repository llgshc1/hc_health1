package com.hcwawe.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.hcwawe.constant.MessageConstant;
import com.hcwawe.constant.RedisConstant;
import com.hcwawe.entity.PageResult;
import com.hcwawe.entity.QueryPageBean;
import com.hcwawe.pojo.Setmeal;
import com.hcwawe.service.SetmealService;
import com.hcwawe.utils.QiniuUtils;
import com.hcwawe.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

//套餐管理
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
//    使用jedispool保存图片名称到jedis
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetmealService setmealService;
//    图片上传七牛云
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        String preName = imgFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString()+preName.substring(preName.length() - 4);
        try{
//            调用七牛云 上传服务器
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }

    }
//    新增套餐·
    @RequestMapping("/add.do")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try{
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        }catch (Exception e){
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }
//    分页查询
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        System.out.println(queryPageBean.getCurrentPage());
       return setmealService.queryPage(queryPageBean);
    }
//    根据id查询 与前端编辑表单绑定
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        try{
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
//    根据setmeal得id查询所绑定得checkgroup
    @RequestMapping("/findCheckItemIdsByCheckGroup")
    public Result findCheckItemIdsByCheckGroup(Integer id){
        try{
            List<Integer> list = setmealService.findCheckItemIdsByCheckGroup(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
//    编辑setmeal 用提交上来的参数
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try{
            setmealService.edit(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.UPDATE_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.UPDATE_SETMEAL_FAIL);
        }
    }
//    删除检查套餐
    @RequestMapping("/delete.do")
    public Result delete(Integer id){
        try{
            setmealService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }
}
