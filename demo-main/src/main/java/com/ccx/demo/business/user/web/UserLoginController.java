package com.ccx.demo.business.user.web;

import com.ccx.demo.business.user.entity.TabUserLogin;
import com.ccx.demo.business.user.service.UserLoginService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import com.support.mvc.web.IController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

/**
 * 请求操作响应：用户登录记录表
 *
 * @author 谢长春 on 2020-03-08
 */
@Api(tags = "用户登录记录表")
@ApiSort(6) // 控制接口排序 <
@RequestMapping("/1/user-login")
@Controller
@Slf4j
@RequiredArgsConstructor
public class UserLoginController implements IController<Long, TabUserLogin> {

    private final UserLoginService service;


    // 优先使用 findByUid 方法，可以阻止平行越权。 只有在实体没有 uid 的情况才能将该方法开放给前端<
    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_find')")
    @ApiOperation(value = "5.按 id 查询用户登录记录表", tags = {"2020-03-08"})
    @ApiOperationSupport(order = 5) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<TabUserLogin> findById(final Long id) {
        return new Result<TabUserLogin>().execute(result -> result.setSuccess(service.findById(id).orElse(null)));
    }


    @GetMapping("/page/{number}/{size}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_page')") // <
    @ApiOperation(value = "6.分页查询用户登录记录表", tags = {"2020-03-08"})
    @ApiOperationSupport(
            order = 6,
            ignoreParameters = {"insertTime", "updateTime", "timestamp", "user"}
    )
    @ResponseBody
    @Override
    public Result<TabUserLogin> page(final int number, final int size, final TabUserLogin condition) {
        return new Result<TabUserLogin>().execute(result -> result.setSuccess(service.findPage(
                Optional.ofNullable(condition).orElseGet(TabUserLogin::new),
                Pager.builder().number(number).size(size).build()
        )));
    }

/*
    // 非必要情况下不要开放列表查询方法，因为没有分页控制，容易内存溢出。大批量查询数据应该使用分页查询<
    @GetMapping
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_search')")
    @ApiOperation(value = "7.分页查询用户登录记录表", tags = {"2020-03-08"})
    @ApiOperationSupport(
            order = 7,
            ignoreParameters = {"insertTime", "updateTime"}
    )
    @ResponseBody
    @Override
    public Result<TabUserLogin> search(final TabUserLogin condition) {
        return new Result<TabUserLogin>().execute(result -> result.setSuccess(service.findList(
                Optional.ofNullable(condition).orElseGet(TabUserLogin::new),
        )));
    }
*/
}
