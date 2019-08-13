package com.ccx.demo.business.user.web;

import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.entity.extend.ITabUser;
import com.ccx.demo.business.user.service.UserService;
import com.ccx.demo.config.init.AppConfig.URL;
import com.ccx.demo.support.web.IAuthController;
import com.support.mvc.entity.base.Param;
import com.support.mvc.entity.base.Result;
import com.utils.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 请求操作响应:用户
 *
 *
 * @author 谢长春 on 2017/11/13.
 */
@RequestMapping("/user/{version}")
@Controller
@Slf4j
public class UserController implements IAuthController<Long> {

    @Autowired
    private UserService service;

    @GetMapping("/current")
    @ResponseBody
    public Result<?> current(@AuthenticationPrincipal final TabUser user, @PathVariable final int version) {
        return new Result<TabUser>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(ITabUser.Props.list())
                        .notes(Arrays.asList(
                                "获取当前登录用户信息"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
                .execute(result -> result
                        .versionAssert(version)
                        .setSuccess(user.toLoginResult())
                );
    }

    @PatchMapping("/nickname")
    @ResponseBody
    public Result<?> updateNickname(@AuthenticationPrincipal final TabUser user,
                                    @PathVariable final int version,
                                    @RequestBody(required = false) final Param param) {
        return new Result<>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(TabUser.Props.list(ITabUser.Props.nickname)) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "修改当前登录用户昵称",
                                "1.初始化"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()), Maps.bySS("nickname", "新的昵称"))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                       .call(() -> service.updateNickname(user.getId(), Param.of(param).required().parseObject().getString("nickname"), user.getId()))
                );
    }
}