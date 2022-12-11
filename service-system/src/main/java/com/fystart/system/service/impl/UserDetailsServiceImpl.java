package com.fystart.system.service.impl;

import com.fystart.model.system.SysUser;
import com.fystart.system.constom.CustomUser;
import com.fystart.system.service.SysMenuService;
import com.fystart.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author fy
 * @date 2022/12/11 16:33
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserInfoByUsername(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }
        //根据userid查询按钮权限值
        List<String> userPermsList = sysMenuService.getUserButtonList(sysUser.getId());
        //转换成SpringSecurity要求格式数据
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String userPerm : userPermsList) {
            authorities.add(new SimpleGrantedAuthority(userPerm));
        }
        return new CustomUser(sysUser, authorities);
    }
}
