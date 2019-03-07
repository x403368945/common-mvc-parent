package com.ccx.security.business.common.web;


import com.alibaba.fastjson.JSONObject;
import com.ccx.security.config.init.AppConfig;
import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.utils.util.Util.isEmpty;

/**
 * 参数配置
 *
 *
 * @author 谢长春 2017年7月21日 上午10:19:28
 */
@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin-config/{version}")
@Slf4j
public class AdminConfigController {

    @GetMapping
    @ResponseBody
    public Result<?> getList(
            @AuthenticationPrincipal final User user,
            @PathVariable final int version) {
        final Result<JSONObject> result = new Result<>(1);
        try {

            final JSONObject obj =  new JSONObject(true);
            for (AppConfig.App key : AppConfig.App.values()) {
                obj.put(key.name(), key.value());
            }
            return result.setSuccess(obj);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setException(e);
        }
        return result;
    }

    @GetMapping("/{key}")
    @ResponseBody
    public Result<?> getByKey(
            @AuthenticationPrincipal final User user,
            @PathVariable final int version, @PathVariable String key) {
        final Result<String> result = new Result<>(1);
        try {
            final AppConfig.App keys = AppConfig.App.valueOf(key);
            if (isEmpty(keys)) {
                throw Code.VALIDATED.exception("key不存在");
            }
            result.setSuccess(keys.value());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setException(e);
        }
        return result;
    }
}