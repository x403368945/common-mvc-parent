package com.ccx.demo.business.user.web;

import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.service.AuthorityService;
import com.ccx.demo.business.user.vo.Authority;
import com.google.common.collect.Lists;
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
                .execute(result -> result
                        .versionAssert(version) // 弱校验版本号
                        .setSuccess(Lists.newArrayList(service.getTree()))
                );
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_Role')")
    @GetMapping("/list")
    @ResponseBody
    public Result<?> list(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version) {
        return new Result<Authority>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version) // 弱校验版本号
                        .setSuccess(service.getList())
                );
    }

}
