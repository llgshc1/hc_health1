package com.hcwawe.dao;

import com.hcwawe.pojo.User;

public interface UserDao {
    public User findByUsername(String username);
}
