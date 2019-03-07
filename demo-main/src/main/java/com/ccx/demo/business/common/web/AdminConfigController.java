package com.ccx.demo.business.common.web;


import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.config.init.AppConfig.App;
import com.ccx.demo.config.init.AppConfig.Path;
import com.ccx.demo.config.init.AppConfig.URL;
import com.support.mvc.entity.base.Item;
import com.support.mvc.entity.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 参数配置
 *
 * @author 谢长春 2017年7月21日 上午10:19:28
 */
@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin-config/{version}")
@Slf4j
public class AdminConfigController {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ExecutorService multiThread;

    @GetMapping("/app")
    @ResponseBody
    public Result<?> getApp(@AuthenticationPrincipal final TabUser user, @PathVariable final int version) {
        return new Result<Item>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .notes(Arrays.asList(
                                "查看应用全局配置参数"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
                .execute(result -> result
                        .versionAssert(version)
                        .setSuccess(Stream.of(App.values())
                                .map(key -> Item.builder().label(key.name()).value(key.value()).comment(key.comment).build())
                                .collect(Collectors.toList())
                        )
                );
    }

    @GetMapping("/path")
    @ResponseBody
    public Result<?> getPath(@AuthenticationPrincipal final TabUser user, @PathVariable final int version) {
        return new Result<Item>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .notes(Arrays.asList(
                                "查看应用目录配置参数"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
                .execute(result -> result
                        .versionAssert(version)
                        .setSuccess(Stream.of(Path.values())
                                .map(key -> Item.builder().label(key.name()).value(key.absolute()).comment(key.comment).build())
                                .collect(Collectors.toList())
                        )
                );
    }

    @GetMapping("/url")
    @ResponseBody
    public Result<?> getUrl(@AuthenticationPrincipal final TabUser user, @PathVariable final int version) {
        return new Result<Item>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .notes(Arrays.asList(
                                "查看应用 URL 配置参数"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
                .execute(result -> result
                        .versionAssert(version)
                        .setSuccess(Stream.of(URL.values())
                                .map(key -> Item.builder().label(key.name()).value(key.value()).comment(key.comment).build())
                                .collect(Collectors.toList())
                        )
                );
    }

    @GetMapping("/{key}")
    @ResponseBody
    public Result<?> getByKey(@PathVariable final int version, @PathVariable String key) {
        return new Result<String>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .notes(Arrays.asList(
                                "查看指定配置参数"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
                .execute(result -> result
                        .versionAssert(version)
                        .setSuccess(App.valueOf(key).value())
                );
    }
}