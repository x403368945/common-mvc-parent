package com.ccx.security.business.common.web;


import com.ccx.security.business.user.entity.TabUser;
import com.ccx.security.config.init.AppConfig.App;
import com.ccx.security.config.init.AppConfig.Path;
import com.ccx.security.config.init.AppConfig.URL;
import com.support.mvc.entity.base.Item;
import com.support.mvc.entity.base.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

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
@RequestMapping("/1/admin-config")
@Slf4j
@RequiredArgsConstructor
public class AdminConfigController {
    private final ApplicationContext applicationContext;
    private final ExecutorService multiThread;

    @GetMapping("/app")
    @ResponseBody
    public Result<?> getApp(@ApiIgnore @AuthenticationPrincipal final TabUser user) {
        return new Result<Item>().execute(result -> result
                .setSuccess(Stream.of(App.values())
                        .map(key -> Item.builder().key(key.name()).value(key.value()).comment(key.comment).build())
                        .collect(Collectors.toList())
                )
        );
    }

    @GetMapping("/path")
    @ResponseBody
    public Result<?> getPath(@ApiIgnore @AuthenticationPrincipal final TabUser user) {
        return new Result<Item>().execute(result -> result
                .setSuccess(Stream.of(Path.values())
                        .map(key -> Item.builder().key(key.name()).value(key.absolute()).comment(key.comment).build())
                        .collect(Collectors.toList())
                )
        );
    }

    @GetMapping("/url")
    @ResponseBody
    public Result<?> getUrl(@ApiIgnore @AuthenticationPrincipal final TabUser user) {
        return new Result<Item>().execute(result -> result
                .setSuccess(Stream.of(URL.values())
                        .map(key -> Item.builder().key(key.name()).value(key.value()).comment(key.comment).build())
                        .collect(Collectors.toList())
                )
        );
    }

    @GetMapping("/{key}")
    @ResponseBody
    public Result<?> getByKey(@ApiIgnore @PathVariable String key) {
        return new Result<String>().execute(result -> result.setSuccess(App.valueOf(key).value()));
    }
}
