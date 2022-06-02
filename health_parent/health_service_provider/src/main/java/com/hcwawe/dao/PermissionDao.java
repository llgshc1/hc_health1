package com.hcwawe.dao;

import com.hcwawe.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    public Set<Permission> findByRoleId(Integer roleId);
}
