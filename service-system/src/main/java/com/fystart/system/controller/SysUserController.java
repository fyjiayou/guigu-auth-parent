package com.fystart.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fystart.common.result.Result;
import com.fystart.common.utils.MD5;
import com.fystart.model.system.SysUser;
import com.fystart.model.vo.SysUserQueryVo;
import com.fystart.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fy
 * @date 2022/12/10 15:02
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
@CrossOrigin
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(httpMethod = "GET", value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
                        @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
                        @ApiParam(name = "userQueryVo", value = "查询对象", required = false) SysUserQueryVo userQueryVo) {
        Page<SysUser> pageParam = new Page<>(page, limit);
        IPage<SysUser> pageModel = sysUserService.selectPage(pageParam, userQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(httpMethod = "GET", value = "获取用户")
    @GetMapping("/getUser/{id}")
    public Result get(@PathVariable String id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    /**
     * 注意：用户密码不能明文存放，需要进行加密
     *
     * @param user
     * @return
     */
    @ApiOperation(httpMethod = "POST", value = "保存用户")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser user) {
        String pwd = MD5.encrypt(user.getPassword());
        user.setPassword(pwd);
        sysUserService.save(user);
        return Result.ok();
    }

    @ApiOperation(httpMethod = "PUT", value = "更新用户")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysUser user) {
        sysUserService.updateById(user);
        return Result.ok();
    }

    @ApiOperation(httpMethod = "DELETE", value = "删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable String id) {
        sysUserService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(httpMethod = "GET", value = "更改用户状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable String id, @PathVariable Integer status) {
        sysUserService.updateStatus(id, status);
        return Result.ok();
    }
}
