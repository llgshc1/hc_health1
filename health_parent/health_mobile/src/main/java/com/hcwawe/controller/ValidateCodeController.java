package com.hcwawe.controller;

import com.aliyuncs.exceptions.ClientException;
import com.hcwawe.constant.MessageConstant;
import com.hcwawe.constant.RedisConstant;
import com.hcwawe.constant.RedisMessageConstant;
import com.hcwawe.entity.Result;
import com.hcwawe.utils.SMSUtils;
import com.hcwawe.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
//    发送用户体检预约验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
//        随机生成验证码
        String validateCode = ValidateCodeUtils.generateValidateCode4String(4);
//        给用户发送验证码
     /*   try {
            //发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }*/
//        将验证码发送到redis(5分钟)
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,60*5,validateCode.toString());

        return new Result(false,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(6);//生成6位数字验证码
        try {
            //发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + code);
        //将生成的验证码缓存到redis
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,
                5 * 60,
                code.toString());
        //验证码发送成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
