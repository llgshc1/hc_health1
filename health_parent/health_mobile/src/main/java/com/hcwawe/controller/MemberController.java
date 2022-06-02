package com.hcwawe.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hcwawe.constant.MessageConstant;
import com.hcwawe.constant.RedisMessageConstant;
import com.hcwawe.entity.Result;
import com.hcwawe.pojo.Member;
import com.hcwawe.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/*处理会员相关操作
* */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String validateInRedis = jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_LOGIN);
        if(validateCode != null && validateCode.length() >0 && validateCode.equals(validateInRedis)){
//            验证码输入正确
//            判断当前用户是否为会员
            Member member = memberService.findByTelephone(telephone);
            if (member == null){
//                不是会员注册
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
//            登陆成功
//            像客户端写入cookie 内容为手机号
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
//            登录信息 保存到redis
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else{
//            验证码输入错误
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }

    }
    }

