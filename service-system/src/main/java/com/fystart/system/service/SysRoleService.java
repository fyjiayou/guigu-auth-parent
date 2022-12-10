package com.fystart.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fystart.model.system.SysRole;
import com.fystart.model.vo.SysRoleQueryVo;

public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取分页列表
     *
     * @param pageParam
     * @param roleQueryVo
     * @return
     */
    IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo roleQueryVo);
}
