package com.ccx.demo.business.user.web;

import com.ccx.demo.business.user.vo.Authority;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.service.AuthorityService;
import com.ccx.demo.config.init.AppConfig.URL;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

/**
 * 请求操作响应：权限指令
 *
 * @author 谢长春 on 2019-08-29
 */
@Controller
@RequestMapping("/authority/{version}")
@Slf4j
public class AuthorityController implements IAuthController<Long> {

    @Autowired
    private AuthorityService service;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_Role')")
    @GetMapping("/tree")
    @ResponseBody
    public Result<?> tree(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version) {
        return new Result<Authority>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(Authority.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "权限指令，url带参说明:/{version【response.version.id】}",
                                "1. 权限指令树"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version) // 弱校验版本号
                        .setSuccess(service.getTree())
                );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_Role')")
    @GetMapping("/list")
    @ResponseBody
    public Result<?> list(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version) {
        return new Result<Authority>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(Authority.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "权限指令，url带参说明:/{version【response.version.id】}",
                                "1. 权限指令列表"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version) // 弱校验版本号
                        .setSuccess(service.getList())
                );
    }

}
