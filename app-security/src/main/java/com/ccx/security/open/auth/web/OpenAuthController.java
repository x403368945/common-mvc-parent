package com.ccx.security.open.auth.web;


import com.alibaba.fastjson.JSON;
import com.ccx.security.business.user.entity.TabUser;
import com.ccx.security.config.init.AppConfig.URL;
import com.ccx.security.enums.Session;
import com.ccx.security.open.auth.entity.AuthLogin;
import com.ccx.security.open.auth.service.AuthService;
import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
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
     * URL:/open/auth/login
     * 参数：param=JSONObject
     *
     * @param version  int 当前请求接口版本号
     * @param body    String
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Result
     */
    @PostMapping("/login")
    @ResponseBody
    public Result<?> login(@PathVariable final int version,
                           @RequestBody final String body,
                           HttpServletRequest request,
                           HttpServletResponse response
    ) {
        final Result<TabUser> result = new Result<>();
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
            AuthLogin authLogin = JSON.parseObject(body, AuthLogin.class);
            switch (authLogin.getMethod()) {
                case SESSION:
                    return loginBySession(version, body, request);
                case TOKEN:
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
     * URL:/open/auth/login/SESSION
     * 参数：param=JSONObject
     *
     * @param version int 当前请求接口版本号
     * @param body   String
     * @param request HttpServletRequest
     * @return Result
     */
    @PostMapping("/login/SESSION")
    @ResponseBody
    public Result<?> loginBySession(@PathVariable final int version,
                                    @RequestBody final String body,
                                    HttpServletRequest request
    ) {
        final Result<TabUser> result = new Result<>(1);
        try {
            result.versionAssert(version);
            AuthLogin authLogin = JSON.parseObject(body, AuthLogin.class);
            // 登录成功之后，将用户信息放入session
            TabUser user = authService.login(authLogin.getUsername(), authLogin.getPassword());
            HttpSession session = request.getSession(true);
//            session.setMaxInactiveInterval(60); // 测试时，设置 session 超时时间为60s
            session.setAttribute(Session.user.name(), user);
            result.setSuccess(user.toLoginResult());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setException(e);
        }
        return result;
    }

//    /**
//     * 以 token 模式登录
//     * URL:/open/auth/login/TOKEN
//     * 参数：param=JSONObject
//     *
//     * @param version int 当前请求接口版本号
//     * @param param  Param
//     * @param request HttpServletRequest
//     * @return Result
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
//                    .version(this.getClass(), builder -> builder
//                            .props(Props.comments())
//                            .notes(Arrays.asList(
//                                    "TOKEN模式登录，最长有效期30天，重新调用该接口则上次的token失效；返回结果extras中存放的是当前有效token，请将该键值对存储在本地，向服务端发起请求时附加到请求头"
//                            ))
//                            .build()
//                            .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()),
//                                    BeanMap.create(new AuthLogin()
//                                            .setUsername("admin")
//                                            .setPassword("111111")
//                                    )
//                            ))
//                    )
//                    .versionAssert(version);
//            AuthLogin authLogin = Param.of(param).required().parseObject(AuthLogin.class);
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
}