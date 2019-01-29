package com.boot.demo.open.auth.web;


import com.boot.demo.business.user.entity.TabUser;
import com.boot.demo.config.init.AppConfig;
import com.boot.demo.enums.Session;
import com.boot.demo.open.auth.entity.AuthLogin;
import com.boot.demo.open.auth.entity.AuthLogin.Props;
import com.boot.demo.open.auth.service.AuthService;
import com.support.mvc.entity.base.Param;
import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * 操作请求处理：授权
 *
 * @author 谢长春
 */
@RequestMapping("/open/auth/{version}")
@Controller
@Slf4j
public class OpenAuthController {

    @Autowired
    private AuthService authService;

    /**
     * 默认以 session 模式登录
     */
    @PostMapping("/login")
    @ResponseBody
    public Result<?> login(
            @PathVariable final int version,
            @RequestBody final Param param,
            HttpServletRequest request,
            HttpServletResponse response) {
        final Result<TabUser> result = new Result<>(1);
        try {
//            log.debug("request.getAuthType():"+request.getAuthType());
//            log.debug("request.getContextPath()"+request.getContextPath());
//            log.debug("request.getPathInfo()"+request.getPathInfo());
//            log.debug("request.getRequestURI()"+request.getRequestURI());
//            log.debug("request.getServletPath()"+request.getServletPath());
//            log.debug("request.getLocalAddr()"+request.getLocalAddr());
//            log.debug("request.getLocalName()"+request.getLocalName());
//            log.debug("request.getProtocol()"+request.getProtocol());
//            log.debug("request.getRemoteAddr()"+request.getRemoteAddr());
//            log.debug("request.getRemoteHost()"+request.getRemoteHost());
//            log.debug("request.getScheme()"+request.getScheme());
//            log.debug("request.getServerName()"+request.getServerName());
//            log.debug("request.getRemotePort()"+request.getRemotePort());
//            log.debug("request.getServerPort()"+request.getServerPort());
//            log.debug("request.getServletContext().getContextPath()"+request.getServletContext().getContextPath());
//            log.debug("request.getServletContext().getServerInfo()"+request.getServletContext().getServerInfo());
//            log.debug("request.getServletContext().getServletContextName()"+request.getServletContext().getServletContextName());
//            log.debug("request.getServletContext().getVirtualServerName()"+request.getServletContext().getVirtualServerName());
            result
                    .version(this.getClass(), builder -> builder
                            .props(Props.list())
                            .notes(Arrays.asList(
                                    "会话模式登录，有效期30分钟"
                            ))
                            .build()
                            .demo(v -> v.setDemo(
                                    AppConfig.URL.SERVER.append(v.formatUrl()),
                                    BeanMap.create(new AuthLogin()
                                            .setUsername("admin")
                                            .setPassword("admin")
                                    )
                            ))
                    )
                    .versionAssert(version);
            final AuthLogin authLogin = Param.of(param).required().parseObject(AuthLogin.class);
            Assert.notNull(authLogin, "登录参数不能为空");
            switch (authLogin.getMethod()) {
                case SESSION:
                    return loginBySession(version, param, request);
//                case TOKEN:
//                    return loginByToken(version, param, request, response);
                case CODE:
                    throw Code.FAILURE.exception("暂不支持手机验证码登录模式");
                default:
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setException(e);
        }
        return result;
    }

    /**
     * 以 session 模式登录
     */
    @PostMapping("/login/SESSION")
    @ResponseBody
    public Result<?> loginBySession(
            @PathVariable final int version,
            @RequestBody final Param param,
            HttpServletRequest request) {
        return new Result<TabUser>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(Props.list())
                        .notes(Arrays.asList(
                                "会话模式登录，有效期30分钟"
                        ))
                        .build()
                        .demo(v -> v.setDemo(
                                AppConfig.URL.SERVER.append(v.formatUrl()),
                                BeanMap.create(new AuthLogin()
                                        .setUsername("admin")
                                        .setPassword("admin")
                                )
                        ))
                )
                .execute(result -> {
                    result.versionAssert(version);
                    final AuthLogin authLogin = Param.of(param).required().parseObject(AuthLogin.class);
                    Assert.notNull(authLogin, "登录参数不能为空");
                    Assert.hasText(authLogin.getUsername(), "用户名不能为空");
                    Assert.hasText(authLogin.getPassword(), "密码不能为空");
                    // 登录成功之后，将用户信息放入session
                    final TabUser user = authService.login(authLogin.getUsername(), authLogin.getPassword());
                    final HttpSession session = request.getSession(true);
//            session.setMaxInactiveInterval(60); // 测试时，设置 session 超时时间为60s
                    session.setAttribute(Session.user.name(), user);
                    result.setSuccess(user.toLoginResult());
                });
    }

//    /**
//     * 以 token 模式登录
//     */
//    @PostMapping("/login/TOKEN")
//    @ResponseBody
//    public Result<?> loginByToken(@PathVariable final int version,
//                                  @RequestBody final Param param,
//                                  HttpServletRequest request,
//                                  HttpServletResponse response
//    ) {
//        final Result<TabUser> result = new Result<>(1);
//        try {
//            result
//                    .version(builder -> builder
//                            .url("/open/auth/{version}/login/TOKEN")
//                            .markdown(this.getClass().getSimpleName().concat("/loginByToken.md"))
//                            .method(POST)
//                            .props(Props.comments())
//                            .notes(Arrays.asList(
//                                    "TOKEN模式登录，最长有效期30天，重新调用该接口则上次的token失效；返回结果extras中存放的是当前有效token，请将该键值对存储在本地，向服务端发起请求时附加到请求头"
//                            ))
//                            .build()
//                            .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()),
//                                    BeanMap.create(new AuthLogin()
//                                            .setUsername("admin")
//                                            .setPassword("admin")
//                                    )
//                                    )
//                            ))
//                    .versionAssert(version);
//            AuthLogin authLogin = Param.of(param).parseObject(AuthLogin.class);
//            Assert.hasText(authLogin.getUsername(), "用户名不能为空");
//            Assert.hasText(authLogin.getPassword(), "密码不能为空");
//            // 登录成功之后，将用户信息放入session； 同时生成 token 回传到前端
//            TabUser user = authService.login(authLogin.getUsername(), authLogin.getPassword());
//            HttpSession session = request.getSession(true);
////            session.setMaxInactiveInterval(60); // 测试时，设置 session 超时时间为60s
//            session.setAttribute(Session.user.name(), user);
//            // 生成token 放在响应头
//            final String token = tokenCache.generate(user);
//            response.setHeader(Session.token.name(), token);
//            result.setSuccess(user.toLoginResult()).addExtras(Session.token.name(), token);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            result.setException(e);
//        }
//        return result;
//    }

//    /**
//     * 激活账户
//     */
//    @GetMapping("/email/{uuid}")
//    public String activate(@PathVariable String uuid) {
//        return authService.activate(uuid);
//    }
//    /**
//     * 忘记密码，发送修改密码邮件
//     */
//    @PatchMapping("/forget/password")
//    @ResponseBody
//    public Result<Object> forgetPassword(@RequestBody(required = false) Param param) {
//        Result<Object> result = new Result<>(1);
//        try {
//            authService.forgetPassword(param.parseObject().getString("email"));
//            result.setCode(SUCCESS);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            result.setCode(e);
//        }
//        return result;
//    }


}