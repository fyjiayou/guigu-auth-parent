package com.fystart.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fystart.common.result.Result;
import com.fystart.model.system.SysRole;
import com.fystart.model.vo.AssginRoleVo;
import com.fystart.model.vo.SysRoleQueryVo;
import com.fystart.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author fy
 * @date 2022/12/10 14:04
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation(httpMethod = "GET", value = "获取全部角色列表")
    @GetMapping("findAll")
    public Result<List<SysRole>> findAll() {
        List<SysRole> roleList = sysRoleService.list();
        return Result.ok(roleList);
    }

    @ApiOperation(httpMethod = "GET", value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
                        @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
                        @ApiParam(name = "roleQueryVo", value = "查询对象", required = false) SysRoleQueryVo roleQueryVo) {
        Page<SysRole> pageParam = new Page<>(page, limit);
        IPage<SysRole> pageModel = sysRoleService.selectPage(pageParam, roleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取角色")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        return Result.ok(role);
    }

    /**
     * @RequestBody不能使用get提交方式
     * 传递json格式的数据，把json格式数据封装到对象里面
     *
     * @param role
     * @return
     */
    @ApiOperation(value = "新增角色")
    @PostMapping("/save")
    public Result save(@RequestBody SysRole role) {
        sysRoleService.save(role);
        return Result.ok();
    }

    @ApiOperation(httpMethod = "PUT", value = "修改角色")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysRole role) {
        sysRoleService.updateById(role);
        return Result.ok();
    }

    @ApiOperation(httpMethod = "DELETE", value = "删除角色")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysRoleService.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除
     * 多个id值[1,2,3]
     * json数组格式 <---> java的list集合
     *
     * @param idList
     * @return
     */
    @ApiOperation(httpMethod = "DELETE", value = "根据id列表删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        sysRoleService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation(httpMethod = "GET",value = "根据用户id获取已分配角色和全部角色")
    @GetMapping("/toAssign/{id}")
    public Result toAssign(@PathVariable String id){
        Map<String,Object> map  = sysRoleService.getUserRoles(id);
        return Result.ok(map);
    }

    @ApiOperation(httpMethod = "POSt",value = "给用户重新分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }
}
