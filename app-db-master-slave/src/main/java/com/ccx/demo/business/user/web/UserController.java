package com.ccx.demo.business.user.web;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.entity.TabUserLogin;
import com.ccx.demo.business.user.service.UserLoginService;
import com.ccx.demo.business.user.service.UserService;
import com.ccx.demo.business.user.vo.TabUserVO;
import com.ccx.demo.config.init.AppConfig.URL;
import com.ccx.demo.enums.Bool;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import com.support.mvc.entity.base.Sorts;
import com.utils.util.Maps;
import com.utils.util.Util;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

import static com.support.mvc.entity.base.Sorts.Direction.DESC;

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
                .version(this.getClass(), builder -> builder
                        .props(TabUser.Props.list())
                        .notes(Arrays.asList(
                                "获取当前登录用户信息"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
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
                .version(this.getClass(), builder -> builder
                        .props(TabUser.Props.list(TabUser.Props.nickname)) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "修改当前登录用户昵称",
                                "1.初始化"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()), Maps.bySS("nickname", "新的昵称"))) // 当前接口参考案例请求地址；
                )
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
                .version(this.getClass(), builder -> builder
                        .props(TabUserLogin.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "查看用户登录记录",
                                "1.初始化"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(1, 20)))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(
                                userLoginService.findPage(
                                        Param.of(json).parseObject(TabUserLogin.class),
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
                .version(this.getClass(), builder -> builder
                        .props(TabUser.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "保存数据，url带参说明:/{version【response.version.id】}",
                                "1.用户表"
                        ))
                        .build()
                        .demo(v -> v.setDemo(
                                URL.SERVER.append(v.formatUrl()), // 当前接口参考案例请求地址；
                                TabUser.builder() // 当前接口参考案例请求参数，一般demo中存放必填字段或者所有字段
                                        .build()
                        ))
                )
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
                .version(this.getClass(), builder -> builder
                                .props(TabUser.Props.list()) // 当前返回对象属性说明
                                .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                        "更新数据，带 uid 强校验，但可能会带当前操作人校验，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}",
                                        "1. 用户表"
                                ))
                                .build()
                                .demo(v -> v.setDemo(
                                        URL.SERVER.append(v.formatUrl(100)), // 当前接口参考案例请求地址；
                                        TabUser.builder() // 当前接口参考案例请求参数，一般demo中存放必填字段或者所有字段
                                                .uid(Util.uuid32())
//                                            .modifyTime(Dates.now().timestamp())
                                                .build()
                                ))
                )
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
                .version(this.getClass(), builder -> builder
                        .props(TabUser.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "按ID + uid 查询单条数据，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}/{id【response.data[*].uid】}",
                                "1. 用户表"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100, Util.uuid32())))) // 当前接口参考案例请求地址；
                )
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
                .version(this.getClass(), builder -> builder
                        .props(TabUser.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "分页查询数据，url带参必须使用 encodeURI 格式化【?json=encodeURI(JSON.stringify({}))】，url带参说明:/{version【response.version.id】}/page/{number【当前页码】}/{size【每页显示条数】}",
                                "1. 用户表"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(1, 20)), // 当前接口参考案例请求地址；
                                TabUser.builder() // 当前接口参考案例请求参数，demo中设置支持查询的字段
                                        .deleted(Bool.NO)
                                        .sorts(Collections.singletonList(Sorts.Order.builder().name(TabUser.OrderBy.id.name()).direction(DESC).build()))
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findPage(
                                Param.of(json).parseObject(TabUser.class),
                                Pager.builder().number(number).size(size).build()
                        ))
                );
    }

}