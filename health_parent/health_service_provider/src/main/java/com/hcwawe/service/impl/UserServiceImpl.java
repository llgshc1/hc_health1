package com.hcwawe.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hcwawe.dao.PermissionDao;
import com.hcwawe.dao.RoleDao;
import com.hcwawe.dao.UserDao;
import com.hcwawe.pojo.Permission;
import com.hcwawe.pojo.Role;
import com.hcwawe.pojo.User;
import com.hcwawe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
//    根据有用户名查询密码 权限列表
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user == null){
            return null;
        }
//        根据用户id查询对应的角色
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
//            根据角色id查询对应的权限
            Integer roleId = role.getId();
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }
}
