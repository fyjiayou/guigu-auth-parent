package com.fystart.system.controller;

import com.fystart.common.helper.JwtHelper;
import com.fystart.common.result.Result;
import com.fystart.common.utils.MD5;
import com.fystart.model.system.SysUser;
import com.fystart.model.vo.LoginVo;
import com.fystart.system.exception.GuiguException;
import com.fystart.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author fy
 * @date 2022/12/10 19:21
 */
@Api(tags = "用户登录接口")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录接口
     * @param loginVo
     * @return
     */
    @ApiOperation(httpMethod = "POST",value = "登录接口")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
        //1.根据用户名称查询数据库
        SysUser sysUser = sysUserService.getUserInfoByUsername(loginVo.getUsername());

        //如果查询为空
        if (Objects.isNull(sysUser)){
            throw new GuiguException(201,"当前用户不存在");
        }

        //判断密码
        if (!MD5.encrypt(loginVo.getPassword()).equals(sysUser.getPassword())){
            throw new GuiguException(201,"用户名或密码错误");
        }

        //判断用户是否可用
        if (sysUser.getStatus() == 0){
            throw new GuiguException(201,"当前用户不可用");
        }

        //根据userid和username生成token字符串,通过map返回
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());

        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }

    /**
     * 获取用户信息
     * @param request
     * @return
     */
    @ApiOperation(httpMethod = "GET",value = "获取用户相关信息")
    @GetMapping("info")
    public Result info(HttpServletRequest request){
        //获取请求头铺token字符串
        String token = request.getHeader("token");

        //从token字符串获取用户名称(id)
        String username = JwtHelper.getUsername(token);

        //根据用户名称获取用户信息
        Map<String,Object> map = sysUserService.getUserInfo(username);

        return Result.ok(map);
    }
}
