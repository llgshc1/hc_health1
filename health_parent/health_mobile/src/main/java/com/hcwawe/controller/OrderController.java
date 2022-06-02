package com.hcwawe.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hcwawe.constant.MessageConstant;
import com.hcwawe.constant.RedisMessageConstant;
import com.hcwawe.entity.Result;
import com.hcwawe.pojo.Order;
import com.hcwawe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/*
* 体检预约处理*/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    /**
     * 体检预约
     * @param map
     * @return
     */
    @RequestMapping("/submit")
public Result submit(@RequestBody Map map){
//        先从redis当中获取保存的验证码 将我们用户输入的验证码 和Redis保存的进行比对
        String telephone = (String) map.get("telephone");
//        对比成功 调用服务完成处理
        String validateCodeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        if (validateCode != null && validateCodeRedis != null && validateCodeRedis.equals(validateCode)){
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            Result result = null;
//            通过duboo
            try {
             result = orderService.order(map);
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }
/*            if(result.isFlag()){
//                给手机发送预约成功消息
            }*/
            return result;

        }else {
//            对比不成功
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
    /**
     * 根据id查询预约信息，包括套餐信息和会员信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Map map = orderService.findById(id);
            //查询预约信息成功
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
