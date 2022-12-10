package com.fystart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fystart.model.system.SysMenu;
import com.fystart.system.exception.GuiguException;
import com.fystart.system.mapper.SysMenuMapper;
import com.fystart.system.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fy
 * @date 2022/12/10 16:08
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    /**
     * 树形显示菜单功能
     *
     * @return
     */
    @Override
    public List<SysMenu> findNodes() {
        //全部数据
        List<SysMenu> sysMenuList = baseMapper.selectList(null);

        List<SysMenu> collect = sysMenuList.stream()
                .filter(item -> item.getParentId() == 0L)
                .map(node -> {
                    node.setChildren(getChildrenNodes(sysMenuList, node));
                    return node;
                }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 递归查找子节点
     *
     * @param menuList
     * @param sysMenu
     * @return
     */
    private List<SysMenu> getChildrenNodes(List<SysMenu> menuList, SysMenu sysMenu) {
        return menuList.stream().filter(item -> item.getParentId().equals(Long.parseLong(sysMenu.getId())))
                .map(node -> {
                    node.setChildren(getChildrenNodes(menuList, node));
                    return node;
                }).collect(Collectors.toList());
    }

    @Override
    public void removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId,id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new GuiguException(201,"请先删除子菜单");
        }
        //调用删除方法
        baseMapper.deleteById(id);
    }
}
