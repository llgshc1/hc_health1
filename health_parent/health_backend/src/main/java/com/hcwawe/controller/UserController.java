package com.hcwawe.controller;

import com.hcwawe.constant.MessageConstant;
import com.hcwawe.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/getUsername.do")
//    登陆成功之后 框架保存登录对象 底层是一个session
    public Result getUserName(){
        try {
                org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println(user);
            if (user != null){
                String username = user.getUsername();
                return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
            }
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }

    }
}
