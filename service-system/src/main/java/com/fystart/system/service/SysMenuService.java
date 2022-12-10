package com.fystart.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fystart.model.system.SysMenu;

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
}
