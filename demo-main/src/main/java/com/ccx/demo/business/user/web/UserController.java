package com.ccx.demo.business.user.web;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.entity.TabUserLogin;
import com.ccx.demo.business.user.service.UserLoginService;
import com.ccx.demo.business.user.service.UserService;
import com.ccx.demo.business.user.vo.TabUserVO;
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
 * 请求操作响应:用户
 *
 * @author 谢长春 on 2017/11/13.
 */
@RequestMapping("/user/{version}")
@Controller
@Slf4j
public class UserController implements IAuthController<Long> {

    @Autowired
    private UserService service;

    @Autowired
    private UserLoginService userLoginService;

    @GetMapping("/current")
    @ResponseBody
    public Result<?> current(@AuthenticationPrincipal final TabUser user,
                             @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version) {
        return new Result<TabUserVO>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version)
                        .setSuccess(user.toTabUserVO())
                );
    }

    @PatchMapping("/nickname")
    @ResponseBody
    public Result<?> updateNickname(@AuthenticationPrincipal final TabUser user,
                                    @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                                    @RequestBody(required = false) final String body) {
        return new Result<>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.updateNickname(user.getId(), JSON.parseObject(body).getString("nickname"), user.getId()))
                );
    }

    @GetMapping("/log/{number}/{size}")
    @ResponseBody
    public Result<?> loginLog(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
            @PathVariable final int number,
            @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final String json) {
        return new Result<>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(
                                userLoginService.findPage(
                                        JSON.parseObject(json, TabUserLogin.class),
                                        Pager.builder().number(number).size(size).build()
                                )
                        )
                );
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'UserController_save')")
    @PostMapping
    @ResponseBody
    public Result<?> save(@AuthenticationPrincipal final TabUser user,
                          @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                          @RequestBody(required = false) final String body) {
        return new Result<>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version) // 弱校验版本号
                        .setSuccess(service.save(
                                JSON.parseObject(body, TabUser.class), user.getId()
                        ))
                );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'UserController_update')")
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
                                JSON.parseObject(body, TabUser.class)
                        ))
                );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'UserController_findByUid')")
    @GetMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> findByUid(@AuthenticationPrincipal final TabUser user,
                               @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                               @PathVariable final Long id,
                               @PathVariable final String uid) {
        return new Result<TabUser>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findByUid(id, uid).orElse(null))
                );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_User')")
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
        return new Result<TabUser>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findPage(
                                JSON.parseObject(json, TabUser.class),
                                Pager.builder().number(number).size(size).build()
                        ))
                );
    }

}