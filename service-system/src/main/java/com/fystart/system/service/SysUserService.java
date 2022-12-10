package com.fystart.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fystart.model.system.SysUser;
import com.fystart.model.vo.SysUserQueryVo;

/**
 * @author fy
 * @date 2022/12/10 15:01
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户信息
     *
     * @param pageParam
     * @param adminQueryVo
     * @return
     */
    IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserQueryVo adminQueryVo);

}
