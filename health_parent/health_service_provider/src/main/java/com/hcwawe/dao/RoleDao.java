package com.hcwawe.dao;

import com.hcwawe.pojo.Role;

import java.util.Set;

public interface RoleDao {
    public Set<Role> findByUserId(Integer userId);
}
