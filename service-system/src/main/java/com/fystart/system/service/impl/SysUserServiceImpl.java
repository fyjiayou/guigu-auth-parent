package com.fystart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fystart.model.system.SysUser;
import com.fystart.model.vo.RouterVo;
import com.fystart.model.vo.SysUserQueryVo;
import com.fystart.system.mapper.SysUserMapper;
import com.fystart.system.service.SysMenuService;
import com.fystart.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fy
 * @date 2022/12/10 15:01
 */
@Transactional
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo userQueryVo){

        return sysUserMapper.selectPage(pageParam, userQueryVo);
    }

    @Override
    public void updateStatus(String id, Integer status) {
        SysUser sysUser = baseMapper.selectById(id);
        sysUser.setStatus(status);
        baseMapper.updateById(sysUser);
    }

    @Override
    public SysUser getUserInfoByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(SysUser::getUsername,username);
        SysUser sysUser = baseMapper.selectOne(queryWrap);
        return sysUser;
    }

    @Override
    public Map<String, Object> getUserInfo(String username) {
        //根据用户名称查询用户信息
        LambdaQueryWrapper<SysUser> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(SysUser::getUsername,username);
        SysUser sysUser = baseMapper.selectOne(queryWrap);

        //根据userId查询菜单权限值
        List<RouterVo> routerVoList = sysMenuService.getUserMenuList(sysUser.getId());

        //根据userId查询按钮权限值
        List<String> permsList = sysMenuService.getUserButtonList(sysUser.getId());

        Map<String,Object> map = new HashMap<>();
        map.put("name",username);
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("roles","[\"admin\"]");

        //菜单权限数据
        map.put("routers",routerVoList);

        //按钮权限数据
        map.put("buttons",permsList);

        return map;
    }
}
