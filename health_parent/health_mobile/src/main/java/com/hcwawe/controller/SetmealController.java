package com.hcwawe.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hcwawe.constant.MessageConstant;
import com.hcwawe.entity.Result;
import com.hcwawe.pojo.Setmeal;
import com.hcwawe.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @RequestMapping("/getAllSetmeal.do")
    public Result getAllSetmeal(){
      try{
          List<Setmeal> list = setmealService.findAll();
          return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
      }catch (Exception e){
          return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
      }
    }
//    查询详情页的套餐所包含的全部信息 检查组检查项
    @RequestMapping("/findSetmealById.do")
    public Result findSetmealById(Integer id){
        try {
            Setmeal setmeal = setmealService.findSetmealById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }
}
