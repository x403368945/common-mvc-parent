package com.ccx.demo.business.user.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.service.UserLoginService;
import com.ccx.demo.business.user.service.UserService;
import com.ccx.demo.business.user.vo.TabUserVO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
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
 * 请求操作响应:用户
 *
 * @author 谢长春 on 2017/11/13.
 */
@Api(tags = "用户")
@ApiSort(3) // 控制接口排序
@RequestMapping("/1/user")
@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController implements IAuthController<Long, TabUser> {

    private final UserService service;

    private final UserLoginService userLoginService;

    @ApiOperation(value = "1.获取当前登录用户", tags = {"2020-03-08"})
    @ApiOperationSupport(order = 1)
    @GetMapping("/current")
    @ResponseBody
    public Result<TabUserVO> current(@ApiIgnore @AuthenticationPrincipal final TabUser user) {
        return new Result<TabUserVO>()
                .execute(result -> result.setSuccess(user.toTabUserVO()));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'UC_save')")
    @ApiOperation(value = "2.新增用户表", tags = {"2020-03-08"})
    @ApiImplicitParam(name = "body", dataType = "TabUser", dataTypeClass = TabUser.class, required = true)
    @ApiOperationSupport(
            order = 2,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
            })
    @ResponseBody
    @Override
    public Result<TabUser> save(final TabUser user, final String body) {
        return new Result<TabUser>().execute(result -> result.setSuccess(
                service.save(JSON.parseObject(body, TabUser.class), user.getId())
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'UC_update')")
    @ApiOperation(value = "3.修改用户表", tags = {"2020-03-08"})
    @ApiImplicitParam(name = "body", dataType = "TabUser", dataTypeClass = TabUser.class, required = true)
    @ApiOperationSupport(
            order = 3,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
            })
    @ResponseBody
    @Override
    public Result<Void> update(final TabUser user, final Long id, final String body) {
        return new Result<Void>().call(() -> service.update(id, user.getId(), JSON.parseObject(body, TabUser.class)));
    }

    @PatchMapping("/{id}/{uid}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'UC_delete')")
    @ApiOperation(value = "4.逻辑删除用户表", tags = {"2020-03-08"})
    @ApiOperationSupport(order = 4) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<Void> markDeleteByUid(final TabUser user, final Long id, final String uid) {
        return new Result<Void>().call(() -> service.markDeleteByUid(id, uid, user.getId()));
    }

    @PatchMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'UC_delete')")
    @ApiOperation(value = "5.批量逻辑删除用户表", tags = {"2020-03-08"})
    @ApiOperationSupport(order = 5) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<Void> markDelete(final TabUser user, final List<MarkDelete> body) {
        return new Result<Void>().call(() -> service.markDelete(body, user.getId()));
    }

    @GetMapping("/{id}/{uid}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_UC', 'UC_findByUid')")
    @ApiOperation(value = "6.按 id 和 uid 查询用户表", tags = {"2020-03-08"})
    @ApiOperationSupport(order = 6) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<TabUser> findByUid(final TabUser user, final Long id, final String uid) {
        return new Result<TabUser>().execute(result -> result.setSuccess(service.findByUid(id, uid).orElse(null)));
    }

    @GetMapping("/page/{number}/{size}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'Menu_UC', 'Menu_page')")
    @ApiOperation(value = "7.分页查询用户表", tags = {"2020-03-08"})
    @ApiOperationSupport(
            order = 7,
            ignoreParameters = {"insertTime", "updateTime"}
    )
    @ResponseBody
    @Override
    public Result<TabUser> page(final TabUser user, final int number, final int size, final TabUser condition) {
        return new Result<TabUser>().execute(result -> result.setSuccess(service.findPage(
                Optional.ofNullable(condition).orElseGet(TabUser::new),
                Pager.builder().number(number).size(size).build()
        )));
    }


    @PatchMapping("/nickname")
    @ApiOperation(value = "8.修改昵称", tags = {"2020-03-08"})
    @ApiOperationSupport(
            order = 8,
            params = @DynamicParameters(name = "UpdateNickname", properties = {
                    @DynamicParameter(name = "nickname", value = "昵称", example = "Jack", required = true, dataTypeClass = String.class),
            })
    )
    @ResponseBody
    public Result<Void> updateNickname(
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @RequestBody final JSONObject body) {
        return new Result<Void>()
                .call(() -> service.updateNickname(
                        user.getId(),
                        body.getString("nickname"),
                        user.getId()
                ));
    }

}
