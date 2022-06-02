package com.hcwawe.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hcwawe.constant.MessageConstant;
import com.hcwawe.entity.PageResult;
import com.hcwawe.entity.QueryPageBean;
import com.hcwawe.entity.Result;
import com.hcwawe.pojo.CheckItem;
import com.hcwawe.service.CheckItemService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
* 检查项统一处理
*@RestController注解 相当于controller加requestbody */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    //    使用doube 调用接口 从zookeeper注册中心调用服务

    @Reference
    private CheckItemService checkItemService;

    //    新增检查项
    @PreAuthorize("hasAnyAuthority('CHECKITEM_ADD')")
    @RequestMapping("/add.do")
//    使用对象接收json数据  @RequestBody注解帮助接收
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
//            进了catch 服务调用失败
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }
//    检查项分页查询
@PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/findPage.do")
    public PageResult findPage (@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQeury(queryPageBean);
        return pageResult;
    }
//    删除项目
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/delete.do")
    public Result delete(Integer id){
        try{
            checkItemService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        }catch (Exception e){
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }

    }
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody CheckItem checkItem){
        try{
            checkItemService.edit(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }

    }
    @RequestMapping("/findAll.do")

    public Result findAll(){
        try {
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }
}
