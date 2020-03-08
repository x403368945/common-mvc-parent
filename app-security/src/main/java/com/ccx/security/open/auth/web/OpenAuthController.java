package com.ccx.security.open.auth.web;


import com.ccx.security.business.user.entity.TabUser;
import com.ccx.security.enums.Session;
import com.ccx.security.open.auth.entity.AuthLogin;
import com.ccx.security.open.auth.service.AuthService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 操作请求处理：授权
 *
 * @author 谢长春
 */
@Api(tags = "登录")
@ApiSort(2)
@RequestMapping("/1/open/auth")
@Controller
@Slf4j
@RequiredArgsConstructor
public class OpenAuthController {

    private final AuthService authService;

    /**
     * 默认以 session 模式登录
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录", tags = {"1.0.0"})
    @ApiOperationSupport(order = 1, ignoreParameters = {"body.method", "body.phone", "body.code"})
    @ResponseBody
    public Result<TabUser> login(@RequestBody final AuthLogin body, HttpServletRequest request) {
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
            Assert.notNull(body, "登录参数不能为空");
            switch (body.getMethod()) {
                case SESSION:
                    return loginBySession(body, request);
//                case TOKEN:
//                    return loginByToken(body, request, response);
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
    @ApiIgnore
    @PostMapping("/login/SESSION")
    @ResponseBody
    public Result<TabUser> loginBySession(@RequestBody final AuthLogin body,
                                            HttpServletRequest request) {
        return new Result<TabUser>()
                .execute(result -> {
                    Assert.notNull(body, "登录参数不能为空");
                    Assert.hasText(body.getUsername(), "用户名不能为空");
                    Assert.hasText(body.getPassword(), "密码不能为空");
                    // 登录成功之后，将用户信息放入session
                    final TabUser user = authService.login(body.getUsername(), body.getPassword());
                    final HttpSession session = request.getSession(true);
//            session.setMaxInactiveInterval(60); // 测试时，设置 session 超时时间为60s
                    session.setAttribute(Session.user.name(), user);
                    result.setSuccess(user.toLoginResult());
                });
    }

//    /**
//     * 以 token 模式登录
//     */
//    @ApiIgnore
//    @PostMapping("/login/TOKEN")
//    @ResponseBody
//    public Result<TabUser> loginByToken(@RequestBody final AuthLogin body,
//                                          HttpServletRequest request,
//                                          HttpServletResponse response
//    ) {
//        final Result<TabUser> result = new Result<>();
//        try {
//            Assert.hasText(body.getUsername(), "用户名不能为空");
//            Assert.hasText(body.getPassword(), "密码不能为空");
//            // 登录成功之后，将用户信息放入session； 同时生成 token 回传到前端
//            TabUser user = authService.login(body.getUsername(), body.getPassword());
//            // 生成token 放在响应头
//            final String token = user.token();
//            response.setHeader(Session.token.name(), token);
//            result.setSuccess(user.toTabUser()).addExtras(Session.token.name(), token);
//
//            Optional.ofNullable(request.getSession(false)).ifPresent(session -> {
//                session.setMaxInactiveInterval(1); // 兼容默认的 session 模式，禁止 token 模式创建 session，设置 session 超时时间为1s
//            });
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            result.setException(e);
//        }
//        return result;
//    }

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
//        final Result<TabUser> result = new Result<>();
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
