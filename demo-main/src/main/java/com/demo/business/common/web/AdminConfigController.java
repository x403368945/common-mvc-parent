package com.demo.business.common.web;


import com.alibaba.fastjson.JSONObject;
import com.demo.business.user.entity.TabUser;
import com.demo.config.init.AppConfig.URL;
import com.mvc.entity.base.Result;
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

import static com.demo.config.init.AppConfig.App;

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

    @GetMapping
    @ResponseBody
    public Result<?> getConfig(@AuthenticationPrincipal final TabUser user, @PathVariable final int version) {
        return new Result<JSONObject>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .notes(Arrays.asList(
                                "查看全部配置参数"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
                .execute(result -> {
                    result.versionAssert(version);
                    final JSONObject obj = new JSONObject(true);
                    for (App key : App.values()) {
                        obj.put(key.name(), key.value());
                    }
                    result.setSuccess(obj);
                });
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