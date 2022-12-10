package com.fystart.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fystart.model.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author fy
 * @date 2022/12/10 16:07
 */
@Repository
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}
