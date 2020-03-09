package com.ccx.demo.business.user.web;

import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.service.AuthorityService;
import com.ccx.demo.business.user.vo.Authority;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.google.common.collect.Lists;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 请求操作响应：权限指令
 *
 * @author 谢长春 on 2019-08-29
 */
@Api(tags = "角色权限指令")
@ApiSort(4) // 控制接口排序
@RequestMapping("/1/authority")
@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthorityController implements IAuthController<Long, Authority> {

    private final AuthorityService service;

    @ApiOperation(value = "1.获取全部权限配置树集合", tags = {"1.0.0"})
    @ApiOperationSupport(order = 1)
    @GetMapping("/tree")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_Role')")
    @ResponseBody
    public Result<Authority> tree(@ApiIgnore @AuthenticationPrincipal final TabUser user) {
        return new Result<Authority>()
                .execute(result -> result.setSuccess(Lists.newArrayList(service.getTree())));
    }

    @ApiOperation(value = "2.获取全部展开后的权限配置集合，可通过 parentCode 构造成权限树", tags = {"1.0.0"})
    @ApiOperationSupport(order = 2)
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_Role')")
    @ResponseBody
    public Result<Authority> list(@ApiIgnore @AuthenticationPrincipal final TabUser user) {
        return new Result<Authority>()
                .execute(result -> result.setSuccess(service.getList()));
    }

}
