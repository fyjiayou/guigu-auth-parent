package com.fystart.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fystart.model.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fy
 * @date 2022/12/10 16:07
 */
@Repository
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户id查询菜单集合
     *
     * @param id
     * @return
     */
    List<SysMenu> findMenuListByUserId(@Param("userId") String id);
}
