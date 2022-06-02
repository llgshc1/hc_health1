package com.hcwawe.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import com.hcwawe.constant.MessageConstant;
import com.hcwawe.entity.PageResult;
import com.hcwawe.entity.QueryPageBean;
import com.hcwawe.entity.Result;
import com.hcwawe.pojo.CheckGroup;
import com.hcwawe.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//检查组操作台
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;
//    增加
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
             try {
                 checkGroupService.add(checkGroup,checkitemIds);
                 return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
             }catch (Exception e){
                 return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
             }

    }
//    分页查询
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkGroupService.pageQuery(queryPageBean);
    }
//    编辑回显
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        try{
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);

        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
//            根据检查组id查询关联的检查项
        }


    }
    @RequestMapping("/findCheckItemIdsByCheckGroup.do")
    public Result findCheckItemIdsByCheckGroup(Integer id){
        try{
            List<Integer> checkitemIds = checkGroupService.findCheckItemIdsByCheckGroup(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemIds);

        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
//            根据检查组id查询关联的检查项
        }


    }
//    处理编辑
@RequestMapping("/edit.do")
public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){

    try {
        checkGroupService.edit(checkGroup,checkitemIds);
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }catch (Exception e){
        return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
    }

}
@RequestMapping("/delete.do")
    public Result delete(Integer id){
        try{
            checkGroupService.delete(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
}
//列出所有的检查组 给套餐复选
@RequestMapping("/findAll.do")
    public Result findAll(){
        try {
           List<CheckGroup> list = checkGroupService.findAll();
           return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
}
}
