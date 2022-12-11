package com.fystart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fystart.model.system.SysMenu;
import com.fystart.model.system.SysRoleMenu;
import com.fystart.model.vo.AssginMenuVo;
import com.fystart.model.vo.RouterVo;
import com.fystart.system.exception.GuiguException;
import com.fystart.system.helper.RouterHelper;
import com.fystart.system.mapper.SysMenuMapper;
import com.fystart.system.mapper.SysRoleMenuMapper;
import com.fystart.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fy
 * @date 2022/12/10 16:08
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

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
        queryWrapper.eq(SysMenu::getParentId, id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new GuiguException(201, "请先删除子菜单");
        }
        //调用删除方法
        baseMapper.deleteById(id);
    }

    /**
     * 树形显示所有的菜单，如果该角色拥有某一菜单，则isSelect为true，默认勾选中
     *
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> getRoleMenus(String roleId) {
        //1.获取所有菜单 status = 1
        LambdaQueryWrapper<SysMenu> queryWrap1 = new LambdaQueryWrapper<>();
        queryWrap1.eq(SysMenu::getStatus, 1);
        List<SysMenu> menuList = baseMapper.selectList(queryWrap1);

        //2.根据角色id查询已分配的菜单
        LambdaQueryWrapper<SysRoleMenu> queryWrap2 = new LambdaQueryWrapper<>();
        queryWrap2.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(queryWrap2);

        //3.从第二步中得到角色分配的菜单id
        List<String> menuIdList = sysRoleMenus.stream().map(item -> item.getMenuId()).collect(Collectors.toList());

        //数据处理：isSelect 如果菜单选择 true
        //拿着分配菜单id 和 所有菜单比对，有相同的，让isSelect值为true
        menuList.stream().forEach(item -> {
            if (menuIdList.contains(item.getId())) {
                item.setSelect(true);
            }
        });

        //4.把menuList组装为树形结构
        List<SysMenu> tree = menuList.stream().filter(item -> item.getParentId() == 0L)
                .map(node -> {
                    node.setChildren(getChildrenNodes(menuList, node));
                    return node;
                }).collect(Collectors.toList());
        return tree;
    }

    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //根据角色id删除菜单权限
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, assginMenuVo.getRoleId());
        sysRoleMenuMapper.delete(queryWrapper);

        //遍历菜单id列表，一个一个进行添加
        List<String> menuIdList = assginMenuVo.getMenuIdList();
        for (String menuId : menuIdList) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuMapper.insert(sysRoleMenu);
        }
    }

    /**
     * 根据用户id查询用户菜单
     * <p>
     * 用户 --> 角色 --> 菜单
     * 用户表 、用户角色表、 角色菜单表 三表查询
     *
     * @param id
     * @return
     */
    @Override
    public List<RouterVo> getUserMenuList(String id) {
        List<SysMenu> sysMenuList = null;

        //admin是超级管理员，操作所有内容
        //如果id = 1，说明是超级管理员，不需要查询了
        if (id.equals("1")) {
            LambdaQueryWrapper<SysMenu> queryWrap1 = new LambdaQueryWrapper<>();
            queryWrap1.eq(SysMenu::getStatus, 1);
            queryWrap1.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(queryWrap1);
        } else {
            sysMenuList = baseMapper.findMenuListByUserId(id);
        }

        //构建树形结构
        List<SysMenu> finalSysMenuList = sysMenuList;
        List<SysMenu> sysMenus = finalSysMenuList.stream().filter(item -> item.getParentId() == 0L)
                .map(node -> {
                    node.setChildren(getChildrenNodes(finalSysMenuList, node));
                    return node;
                }).collect(Collectors.toList());

        //转换成前端要求的格式
        List<RouterVo> routerVoList = RouterHelper.buildRouters(sysMenus);

        return routerVoList;
    }

    @Override
    public List<String> getUserButtonList(String userId) {
        List<SysMenu> sysMenuList = null;

        //admin是超级管理员，操作所有内容
        //如果id = 1，说明是超级管理员，不需要查询了
        if (userId.equals("1")) {
            LambdaQueryWrapper<SysMenu> queryWrap1 = new LambdaQueryWrapper<>();
            queryWrap1.eq(SysMenu::getStatus, 1);
            queryWrap1.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(queryWrap1);
        } else {
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }

        //遍历菜单集合
        List<String> permissionList = new ArrayList<>();
        sysMenuList.stream().forEach(item ->{
            if (item.getType() == 2){
                permissionList.add(item.getPerms());
            }
        });

        return permissionList;
    }
}
