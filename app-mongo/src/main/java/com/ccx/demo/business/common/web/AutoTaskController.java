package com.ccx.demo.business.common.web;


import com.ccx.demo.business.common.service.AutoTaskService;
import com.ccx.demo.business.user.entity.TabUser;
import com.support.mvc.entity.base.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 定时任务
 *
 * @author 谢长春 2017年7月21日 上午10:19:28
 */
@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/1/auto-task")
@Slf4j
@RequiredArgsConstructor
public class AutoTaskController {

    private final AutoTaskService service;

    @PostMapping("/clear-temp-directory")
    @ResponseBody
    public Result<Void> clearTempDirectory(@ApiIgnore @AuthenticationPrincipal final TabUser user) {
        return new Result<Void>().call(service::clearTempDirectory);
    }

}
