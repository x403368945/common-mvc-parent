package com.ccx.demo.business.user.web;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.user.entity.TabRole;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.service.RoleService;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 请求操作响应：角色表
 *
 * @author 谢长春 on 2019-08-29
 */
@Controller
@RequestMapping("/role/{version}")
@Slf4j
public class RoleController implements IAuthController<Long> {

    @Autowired
    private RoleService service;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'RoleController', 'RoleController_save')")
    @PostMapping
    @ResponseBody
    @Override
    public Result<?> save(@AuthenticationPrincipal final TabUser user,
                          @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                          // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
                          @RequestBody(required = false) final String body) {
        return new Result<TabRole>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.save(
                                JSON.parseObject(body, TabRole.class),
                                user.getId()
                        ))
                );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'RoleController', 'RoleController_update')")
    @PutMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> update(@AuthenticationPrincipal final TabUser user,
                            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                            @PathVariable final Long id,
                            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
                            @RequestBody(required = false) final String body) {
        return new Result<>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.update(
                                id,
                                user.getId(),
                                JSON.parseObject(body, TabRole.class)
                        ))
                );
    }

    @PatchMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> markDeleteByUid(@AuthenticationPrincipal final TabUser user,
                                     @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                                     @PathVariable final Long id,
                                     @PathVariable final String uid) {
        return new Result<>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.markDeleteByUid(id, uid, user.getId()))
                );
    }

    @GetMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> findByUid(@AuthenticationPrincipal final TabUser user,
                               @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                               @PathVariable final Long id,
                               @PathVariable final String uid) {
        return new Result<TabRole>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findByUid(id, uid).orElse(null))
                );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_Role', 'RoleController', 'RoleController_page')")
    @GetMapping("/page/{number}/{size}")
    @ResponseBody
    @Override
    public Result<?> page(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
            @PathVariable final int number,
            @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final String json
    ) {
        return new Result<TabRole>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findPage(
                                JSON.parseObject(json, TabRole.class),
                                Pager.builder().number(number).size(size).build()
                        ))
                );
    }

    @GetMapping("/options")
    @ResponseBody
    public Result<?> options(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version) {
        return new Result<TabRole>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version) // 弱校验版本号
                        .setSuccess(service.getOptions())
                );
    }

}
