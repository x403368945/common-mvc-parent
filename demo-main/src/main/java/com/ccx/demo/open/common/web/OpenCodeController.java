package com.ccx.demo.open.common.web;

import com.ccx.demo.config.init.AppConfig;
import com.ccx.demo.enums.Session;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import com.utils.util.CodeImage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * 验证码接口
 *
 * @author 谢长春 on 2017-9-18
 */
@Api(tags = "图片验证码")
@ApiSort(1)
@RequestMapping("/1/open/code")
@Controller
@Slf4j
@RequiredArgsConstructor
public class OpenCodeController {
    private final AppConfig appConfig;

//    private EmailCode emailCode;
//    private PhoneCode phoneCode;

    @GetMapping
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "获取图片验证码", tags = {"0.0.1"}, notes = "验证码图片以 base64 格式放在 data 数组中返回")
    @ResponseBody
    public Result<String> getImageCode(HttpServletRequest request) {
        return new Result<String>().execute(result -> {
            final HttpSession session = request.getSession(true);
            result.setSuccess(CodeImage.ofDefault()
                    .generate(code -> {
                        session.setAttribute(Session.code.name(), code);
                        if (appConfig.isDev() || appConfig.isBeta()) {
                            result.addExtras("code", code);
                        }
                    })
                    .base64()
            );
        });
    }

    @ApiOperation(value = "校验图片验证码", tags = {"0.0.0"}, notes = "仅用于开发环境测试图片验证码输入")
    @GetMapping("/check/{code}")
    @ResponseBody
    @Deprecated
    public Result<String> checkImageCode(@ApiParam(required = true, value = "验证码", example = "a1b2") @PathVariable String code, HttpServletRequest request) {
        return new Result<String>().execute(result -> {
            final HttpSession session = request.getSession(true);
            if (Objects.equals(code, Session.code.get(session).orElse(null))) {
                result.setCode(Code.SUCCESS);
            } else { // 重新生成验证码
                result
                        .setSuccess(CodeImage.ofDefault()
                                .generate(value -> {
                                    session.setAttribute(Session.code.name(), value);
                                    if (appConfig.isDev() || appConfig.isBeta()) {
                                        result.addExtras("code", value);
                                    }
                                })
                                .base64()
                        )
                        .setCode(Code.IMAGE_CODE)
                ;
            }
        });
    }

}
