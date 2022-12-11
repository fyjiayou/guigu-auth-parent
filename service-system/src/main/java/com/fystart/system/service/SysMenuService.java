package com.fystart.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fystart.model.system.SysMenu;
import com.fystart.model.vo.AssginMenuVo;
import com.fystart.model.vo.RouterVo;

import java.util.List;

/**
 * @author fy
 * @date 2022/12/10 16:08
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 菜单树形数据
     *
     * @return
     */
    List<SysMenu> findNodes();

    /**
     * 删除菜单，若菜单有子菜单则不能删除
     *
     * @param id
     */
    void removeMenuById(Long id);

    /**
     * 根据角色获取菜单
     * @param roleId
     * @return
     */
    List<SysMenu> getRoleMenus(String roleId);

    /**
     * 给角色分配菜单
     * @param assginMenuVo
     */
    void doAssign(AssginMenuVo assginMenuVo);

    /**
     * 根据用户id获取相应的菜单列表
     * @param userId
     * @return
     */
    List<RouterVo> getUserMenuList(String userId);

    /**
     * 获取用户按钮权限
     * @param userId
     * @return
     */
    List<String> getUserButtonList(String userId);
}
