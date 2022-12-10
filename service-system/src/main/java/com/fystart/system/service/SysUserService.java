package com.fystart.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fystart.model.system.SysUser;
import com.fystart.model.vo.SysUserQueryVo;

import java.util.Map;

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

    /**
     * 更改用户状态
     * @param id
     * @param status
     */
    void updateStatus(String id, Integer status);

    /**
     * 根据用户名查询数据库
     * @param username
     * @return
     */
    SysUser getUserInfoByUsername(String username);

    Map<String, Object> getUserInfo(String username);
}
