package com.ccx.demo.open.auth.web;

import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.vo.TabUserVO;
import com.ccx.demo.enums.Session;
import com.ccx.demo.open.auth.service.AuthService;
import com.ccx.demo.open.auth.vo.AuthLogin;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

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
    public Result<TabUserVO> login(
            @RequestBody final AuthLogin body,
            HttpServletRequest request,
            HttpServletResponse response) {
        final Result<TabUserVO> result = new Result<>();
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
                case TOKEN:
                    return loginByToken(body, request, response);
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
    public Result<TabUserVO> loginBySession(@RequestBody final AuthLogin body,
                                            HttpServletRequest request) {
        return new Result<TabUserVO>()
                .execute(result -> {
                    Assert.notNull(body, "登录参数不能为空");
                    Assert.hasText(body.getUsername(), "用户名不能为空");
                    Assert.hasText(body.getPassword(), "密码不能为空");
                    // 登录成功之后，将用户信息放入session
                    final TabUser user = authService.login(body.getUsername(), body.getPassword());
                    final HttpSession session = request.getSession(true);
//            session.setMaxInactiveInterval(60); // 测试时，设置 session 超时时间为60s
                    session.setAttribute(Session.user.name(), user);
                    result.setSuccess(user.toTabUserVO());
                });
    }

    /**
     * 以 token 模式登录
     */
    @ApiIgnore
    @PostMapping("/login/TOKEN")
    @ResponseBody
    public Result<TabUserVO> loginByToken(@RequestBody final AuthLogin body,
                                          HttpServletRequest request,
                                          HttpServletResponse response
    ) {
        final Result<TabUserVO> result = new Result<>();
        try {
            Assert.hasText(body.getUsername(), "用户名不能为空");
            Assert.hasText(body.getPassword(), "密码不能为空");
            // 登录成功之后，将用户信息放入session； 同时生成 token 回传到前端
            TabUser user = authService.login(body.getUsername(), body.getPassword());
            // 生成token 放在响应头
            final String token = user.token();
            response.setHeader(Session.token.name(), token);
            result.setSuccess(user.toTabUserVO()).addExtras(Session.token.name(), token);

            Optional.ofNullable(request.getSession(false)).ifPresent(session -> {
                session.setMaxInactiveInterval(1); // 兼容默认的 session 模式，禁止 token 模式创建 session，设置 session 超时时间为1s
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setException(e);
        }
        return result;
    }

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
//    public Result<Object> forgetPassword(@RequestBody(required = false) String body) {
//        Result<Object> result = new Result<>();
//        try {
//            authService.forgetPassword(JSON.parseObject(body).getString("email"));
//            result.setCode(SUCCESS);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            result.setCode(e);
//        }
//        return result;
//    }


}