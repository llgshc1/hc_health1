package com.hcwawe.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hcwawe.pojo.Permission;
import com.hcwawe.pojo.Role;
import com.hcwawe.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Component
public class SpringSecurityUserService implements UserDetailsService {
//    使用dubbo使用网路远程调用服务提供方
//    根据用户名查询数据库 获取用户信息
    @Reference
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findByUsername(s);
        if (user == null){
//            用户名不存在 直接返回空
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<>();
//        动态授权
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //        遍历角色集合 为用户授予角色

            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
//                授权
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
            }
        }
        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(s,user.getPassword(),list);
        
        return securityUser;
    }
}
