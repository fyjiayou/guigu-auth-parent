package com.fystart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fystart.model.system.SysRole;
import com.fystart.model.system.SysUserRole;
import com.fystart.model.vo.AssginRoleVo;
import com.fystart.model.vo.SysRoleQueryVo;
import com.fystart.system.mapper.SysRoleMapper;
import com.fystart.system.mapper.SysUserRoleMapper;
import com.fystart.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo roleQueryVo) {
        return sysRoleMapper.selectPage(pageParam, roleQueryVo);
    }

    @Override
    public Map<String, Object> getUserRoles(String id) {
        //获取全部角色
        List<SysRole> roles = baseMapper.selectList(null);

        //查询用户已经分配的角色
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId,id);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(queryWrapper);

        //从sysUserRoles集合获取所有角色id
        List<String> userRoleIds = new ArrayList<>();
        for (SysUserRole sysUserRole : sysUserRoles) {
            userRoleIds.add(sysUserRole.getRoleId());
        }

        Map<String,Object> map = new HashMap<>();
        //所有角色
        map.put("allRoles",roles);
        //用户分配角色id集合
        map.put("userRoleIds",sysUserRoles);
        return map;
    }

    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //删除之前已经分配的角色
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole ::getUserId,assginRoleVo.getUserId());
        sysUserRoleMapper.delete(queryWrapper);

        //保存新分配的角色
        List<String> roleIdList = assginRoleVo.getRoleIdList();
        for (String roleId : roleIdList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleMapper.insert(sysUserRole);
        }
    }
}
