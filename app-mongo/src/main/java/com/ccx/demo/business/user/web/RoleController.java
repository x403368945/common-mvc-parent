package com.ccx.demo.business.user.web;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.user.entity.TabRole;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.service.RoleService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;

/**
 * 请求操作响应：角色表
 *
 * @author 谢长春 on 2019-08-29
 */
@Api(tags = "角色")
@ApiSort(3) // 控制接口排序
@RequestMapping("/1/role")
@Controller
@Slf4j
@RequiredArgsConstructor
public class RoleController implements IAuthController<Long, TabRole> {

    private final RoleService service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'RC_save')")
    @ApiOperation(value = "3.新增角色", tags = {"0.0.1"})
    @ApiImplicitParam(name = "body", dataType = "TabRole", dataTypeClass = TabRole.class, required = true)
    @ApiOperationSupport(
            order = 3,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
                    , "body.authorities"
            })
    @ResponseBody
    @Override
    public Result<TabRole> save(final TabUser user, final String body) {
        return new Result<TabRole>().execute(result -> result.setSuccess(
                service.save(JSON.parseObject(body, TabRole.class), user.getId())
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'RC_update')")
    @ApiOperation(value = "4.修改角色", tags = {"0.0.1"})
    @ApiImplicitParam(name = "body", dataType = "TabRole", dataTypeClass = TabRole.class, required = true)
    @ApiOperationSupport(
            order = 4,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
                    , "body.authorities"
            })
    @ResponseBody
    @Override
    public Result<Void> update(final TabUser user, final Long id, final String body) {
        return new Result<Void>().call(() -> service.update(id, user.getId(), JSON.parseObject(body, TabRole.class)));
    }

    @PatchMapping("/{id}/{uid}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'RC_delete')")
    @ApiOperation(value = "5.逻辑删除角色", tags = {"0.0.1"})
    @ApiOperationSupport(order = 5)
    @ResponseBody
    @Override
    public Result<Void> markDeleteByUid(final TabUser user, final Long id, final String uid) {
        return new Result<Void>().call(() -> service.markDeleteByUid(id, uid, user.getId()));
    }

    @PatchMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'RC_delete')")
    @ApiOperation(value = "6.批量逻辑删除角色表", tags = {"0.0.1"})
    @ApiOperationSupport(order = 6) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<Void> markDelete(final TabUser user, final List<MarkDelete> body) {
        return new Result<Void>().call(() -> service.markDelete(body, user.getId()));
    }

    @GetMapping("/{id}/{uid}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_RC', 'RC_findByUid')")
    @ApiOperation(value = "7.按 id 和 uid 查询角色", tags = {"0.0.1"})
    @ApiOperationSupport(order = 7)
    @ResponseBody
    @Override
    public Result<TabRole> findByUid(final TabUser user, final Long id, final String uid) {
        return new Result<TabRole>().execute(result -> result.setSuccess(service.findByUid(id, uid).orElse(null)));
    }

    @GetMapping("/page/{number}/{size}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_RC', 'RC_page')")
    @ApiOperation(value = "8.分页查询角色", tags = {"0.0.1"})
    @ApiOperationSupport(
            order = 8,
            ignoreParameters = {"insertTime", "updateTime", "authorityTree", "authorities"}
    )
    @ResponseBody
    @Override
    public Result<TabRole> page(final TabUser user, final int number, final int size, final TabRole condition) {
        return new Result<TabRole>().execute(result -> result.setSuccess(service.findPage(
                Optional.ofNullable(condition).orElseGet(TabRole::new),
                Pager.builder().number(number).size(size).build()
        )));
    }

    @GetMapping("/options")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_RC', 'RC_options')")
    @ApiOperation(value = "9.获取所有有效角色列表", tags = {"0.0.1"})
    @ApiOperationSupport(order = 9)
    @ResponseBody
    public Result<TabRole> options(@ApiIgnore @AuthenticationPrincipal final TabUser user) {
        return new Result<TabRole>().execute(result -> result.setSuccess(service.getOptions()));
    }

}
