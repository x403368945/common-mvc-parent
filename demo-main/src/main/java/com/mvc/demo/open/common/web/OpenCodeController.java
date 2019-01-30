package com.mvc.demo.open.common.web;

import com.mvc.demo.config.init.AppConfig;
import com.mvc.demo.config.init.AppConfig.URL;
import com.mvc.demo.enums.Session;
import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import com.support.mvc.web.IController;
import com.utils.util.CodeImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Objects;

/**
 * 验证码接口
 *
 *
 * @author 谢长春 on 2017-9-18
 */
@RequestMapping("/open/code/{version}")
@Controller
@Slf4j
public class OpenCodeController implements IController<String> {

    //    @Autowired
//    private EmailCode emailCode;
//    @Autowired
//    private PhoneCode phoneCode;

    @GetMapping
    @ResponseBody
    public Result<?> getImageCode(@PathVariable final int version, HttpServletRequest request) {
        return new Result<String>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .notes(Arrays.asList(
                                "获取图片验证码，将图片验证码转换为 base64 格式后返回",
                                "开发环境，将图片验证码放在{extras:{code:验证码}}；方便自动化测试"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
                .execute(result -> {
                    result.versionAssert(version);
                    final HttpSession session = request.getSession(true);
                    result.setSuccess(
                            CodeImage.ofDefault()
                                    .generate(code -> {
                                        session.setAttribute(Session.code.name(), code);
                                        if (AppConfig.isDev() || AppConfig.isBeta()) {
                                            result.addExtras("code", code);
                                        }
                                    })
                                    .base64()
                    );
                });
    }

    @GetMapping("/check/{code}")
    @ResponseBody
    public Result<?> checkImageCode(@PathVariable final int version, @PathVariable String code, HttpServletRequest request) {
        return new Result<String>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .notes(Arrays.asList(
                                "校验图片验证码",
                                "验证失败后，将会返回新的验证码，不需要重新调用获取验证码接口"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
                .execute(result -> {
                    result.versionAssert(version);
                    final HttpSession session = request.getSession(true);
                    if (Objects.equals(code, Session.code.get(session).orElse(null))) {
                        result.setCode(Code.SUCCESS);
                    } else { // 重新生成验证码
                        result
                                .setSuccess(
                                        CodeImage.ofDefault()
                                                .generate(value -> {
                                                    session.setAttribute(Session.code.name(), value);
                                                    if (AppConfig.isDev() || AppConfig.isBeta()) {
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