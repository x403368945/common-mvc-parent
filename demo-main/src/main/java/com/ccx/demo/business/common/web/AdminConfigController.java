package com.ccx.demo.business.common.web;


import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.config.init.AppConfig.App;
import com.ccx.demo.config.init.AppConfig.Path;
import com.ccx.demo.config.init.AppConfig.URL;
import com.support.mvc.entity.base.Item;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.ApiParam;
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
    public Result<?> getApp(@AuthenticationPrincipal final TabUser user,
                            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version) {
        return new Result<Item>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version)
                        .setSuccess(Stream.of(App.values())
                                .map(key -> Item.builder().key(key.name()).value(key.value()).comment(key.comment).build())
                                .collect(Collectors.toList())
                        )
                );
    }

    @GetMapping("/path")
    @ResponseBody
    public Result<?> getPath(@AuthenticationPrincipal final TabUser user,
                             @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version) {
        return new Result<Item>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version)
                        .setSuccess(Stream.of(Path.values())
                                .map(key -> Item.builder().key(key.name()).value(key.absolute()).comment(key.comment).build())
                                .collect(Collectors.toList())
                        )
                );
    }

    @GetMapping("/url")
    @ResponseBody
    public Result<?> getUrl(@AuthenticationPrincipal final TabUser user,
                            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version) {
        return new Result<Item>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version)
                        .setSuccess(Stream.of(URL.values())
                                .map(key -> Item.builder().key(key.name()).value(key.value()).comment(key.comment).build())
                                .collect(Collectors.toList())
                        )
                );
    }

    @GetMapping("/{key}")
    @ResponseBody
    public Result<?> getByKey(
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version, @PathVariable String key) {
        return new Result<String>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version)
                        .setSuccess(App.valueOf(key).value())
                );
    }
}