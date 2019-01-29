package com.boot.demo.business.user.web;

import com.boot.demo.business.user.entity.TabUser;
import com.boot.demo.business.user.entity.extend.ITabUser.Props;
import com.boot.demo.business.user.service.UserService;
import com.boot.demo.config.init.AppConfig.URL;
import com.boot.demo.support.web.IAuthController;
import com.support.mvc.entity.base.Param;
import com.support.mvc.entity.base.Result;
import com.utils.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

import static com.boot.demo.business.user.entity.extend.ITabUser.Props.nickname;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

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
                .version(builder -> builder
                        .url("/user/{version}/current")
                        .markdown(this.getClass().getSimpleName().concat("/current.md"))
                        .method(GET)
                        .props(Props.list())
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
                .version(builder -> builder
                        .url("/demo-list/{version}/nickname") // 当前请求接口
                        .markdown(this.getClass().getSimpleName().concat("/updateNickname.md")) // 接口说明文档地址
                        .method(PATCH) // 当前接口请求方式
                        .props(TabUser.Props.list(nickname)) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "修改当前登录用户昵称",
                                "1.初始化"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()), Maps.bySS("nickname", "新的昵称"))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .execute(() -> service.updateNickname(user.getId(), Param.of(param).required().parseObject().getString("nickname"), user.getId()))
                );
    }
}