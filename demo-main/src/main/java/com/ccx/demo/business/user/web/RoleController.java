package com.ccx.demo.business.user.web;

import com.ccx.demo.business.user.entity.TabRole;
import com.ccx.demo.business.user.entity.TabRole.OrderBy;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.service.RoleService;
import com.ccx.demo.config.init.AppConfig.URL;
import com.ccx.demo.enums.Radio;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Param;
import com.support.mvc.entity.base.Result;
import com.support.mvc.entity.base.Sorts;
import com.utils.util.Util;
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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'RoleController_save')")
    @PostMapping
    @ResponseBody
    @Override
    public Result<?> save(@AuthenticationPrincipal final TabUser user,
                          @PathVariable final int version,
                          // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
                          @RequestBody(required = false) final Param param) {
        return new Result<TabRole>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(TabRole.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "保存数据，url带参说明:/{version【response.version.id】}",
                                "1. 角色表"
                        ))
                        .build()
                        .demo(v -> v.setDemo(
                                URL.SERVER.append(v.formatUrl()), // 当前接口参考案例请求地址；
                                TabRole.builder() // 当前接口参考案例请求参数，一般demo中存放必填字段或者所有字段
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.save(
                                Param.of(param).required().parseObject(TabRole.class),
                                user.getId()
                        ))
                );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'RoleController_update')")
    @PutMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> update(@AuthenticationPrincipal final TabUser user,
                            @PathVariable final int version,
                            @PathVariable final Long id,
                            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
                            @RequestBody(required = false) final Param param) {
        return new Result<>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                                .props(TabRole.Props.list()) // 当前返回对象属性说明
                                .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                        "更新数据，不带 uid 强校验，但可能会带当前操作人校验，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}",
                                        "1. 角色表"
                                ))
                                .build()
                                .demo(v -> v.setDemo(
                                        URL.SERVER.append(v.formatUrl(100)), // 当前接口参考案例请求地址；
                                        TabRole.builder() // 当前接口参考案例请求参数，一般demo中存放必填字段或者所有字段
                                                .uid(Util.uuid32())
//                                            .updateTime(Dates.now().timestamp())
                                                .build()
                                ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.update(
                                id,
                                user.getId(),
                                Param.of(param).required().parseObject(TabRole.class)
                        ))
                );
    }

    @PatchMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> markDeleteByUid(@AuthenticationPrincipal final TabUser user,
                                     @PathVariable final int version,
                                     @PathVariable final Long id,
                                     @PathVariable final String uid) {
        return new Result<>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(TabRole.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "逻辑删除数据，带 uid 强校验，也可能会带当前操作人校验，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}/{id【response.data[*].uid】}",
                                "1. 角色表"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100, Util.uuid32())))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.markDeleteByUid(id, uid, user.getId()))
                );
    }

    @GetMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> findByUid(@AuthenticationPrincipal final TabUser user,
                               @PathVariable final int version,
                               @PathVariable final Long id,
                               @PathVariable final String uid) {
        return new Result<TabRole>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(TabRole.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "按ID + uid 查询单条数据，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}/{id【response.data[*].uid】}",
                                "1. 角色表"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100, Util.uuid32())))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findByUid(id, uid).orElse(null))
                );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_Role', 'RoleController_page')")
    @GetMapping("/page/{number}/{size}")
    @ResponseBody
    @Override
    public Result<?> page(
            @AuthenticationPrincipal final TabUser user,
            @PathVariable final int version,
            @PathVariable final int number,
            @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final String json
    ) {
        return new Result<TabRole>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(TabRole.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "分页查询数据，url带参必须使用 encodeURI 格式化【?json=encodeURI(JSON.stringify({}))】，url带参说明:/{version【response.version.id】}/page/{number【当前页码】}/{size【每页显示条数】}",
                                "1. 角色表"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(1, 20)), // 当前接口参考案例请求地址；
                                TabRole.builder() // 当前接口参考案例请求参数，demo中设置支持查询的字段
                                        .deleted(Radio.NO)
                                        .sorts(Collections.singletonList(Sorts.Order.builder().name(OrderBy.id.name()).direction(DESC).build()))
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findPage(
                                Param.of(json).parseObject(TabRole.class),
                                Pager.builder().number(number).size(size).build()
                        ))
                );
    }

    @GetMapping("/options")
    @ResponseBody
    public Result<?> options(
            @AuthenticationPrincipal final TabUser user,
            @PathVariable final int version) {
        return new Result<TabRole>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(TabRole.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "前端角色下拉选项，url带参说明:/{version【response.version.id】}",
                                "1. 角色表"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version) // 弱校验版本号
                        .setSuccess(service.getOptions())
                );
    }

}
