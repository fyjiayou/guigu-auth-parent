package com.fystart.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fystart.model.system.SysRole;
import com.fystart.model.vo.AssginRoleVo;
import com.fystart.model.vo.SysRoleQueryVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取分页列表
     *
     * @param pageParam
     * @param roleQueryVo
     * @return
     */
    IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo roleQueryVo);

    /**
     * 根据用户id获取已分配角色和全部角色
     * @param id
     * @return
     */
    Map<String, Object> getUserRoles(String id);

    /**
     * 给用户重新分配角色
     * @param assginRoleVo
     */
    void doAssign(AssginRoleVo assginRoleVo);
}
