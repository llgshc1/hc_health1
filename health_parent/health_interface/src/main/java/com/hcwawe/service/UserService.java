package com.hcwawe.service;

import com.hcwawe.pojo.User;

public interface UserService {
//    用户名提供查询
    public User findByUsername(String username);
}
