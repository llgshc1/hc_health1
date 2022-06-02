package com.hcwawe.service;

import com.hcwawe.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringSecurityUserService implements UserDetailsService {
    //模拟数据库中的用户数据
    public  static Map<String, User> map = new HashMap<>();
    static {
        com.hcwawe.pojo.User user1 = new com.hcwawe.pojo.User();
        user1.setUsername("admin");
        user1.setPassword("admin");

        com.hcwawe.pojo.User user2 = new com.hcwawe.pojo.User();
        user2.setUsername("xiaoming");
        user2.setPassword("1234");

        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }
    /**
     * 根据用户名加载用户信息
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
//    根据username 加载用户
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("用户输入的用户名为："+s);
//        查询用户信息 （模拟）
        User user = map.get(s);
        if (user == null){
//            用户名不存在
            return null;
        }else {
//            将用户信息返回给框架
//            框架会进行密码比对
            List<GrantedAuthority> list = new ArrayList<>();
            list.add(new SimpleGrantedAuthority("permission_A"));//授权
            list.add(new SimpleGrantedAuthority("permission_B"));
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//授予角色
            org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(s, "{noop}"+user.getPassword(), list);
            return securityUser;
        }
//        用户信息返回给框架
//        框架进行密码比对（页面提交的密码和数据库查询出的密码）

    }
}
