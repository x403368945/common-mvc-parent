package com.support.config.security;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.support.mvc.entity.base.Param;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;

/**
 * 自定义登录过滤器，扩展 {@link UsernamePasswordAuthenticationFilter}; 支持 json 参数登录
 * <pre>
 *     json 登录请求:
 *     header -> Content-Type: application/json
 *     body -> {"json":{"username":"admin", "password":"admin"}}
 *
 *
 * @author 谢长春 2018/11/16
 */
@Slf4j
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ThreadLocal<String> TL = new ThreadLocal<>();

    public JsonUsernamePasswordAuthenticationFilter(final AuthenticationManager authenticationManager,
                                                    final AuthenticationSuccessHandler successHandler,
                                                    final AuthenticationFailureHandler failureHandler) {
        super();
        super.setAuthenticationManager(authenticationManager);
        super.setAuthenticationSuccessHandler(successHandler);
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        try {
            if (Optional.ofNullable(request.getHeader("Content-Type"))
                    .map(v -> v.startsWith("application/json") ? v : null)
                    .isPresent()) {
                TL.set(CharStreams.toString(new InputStreamReader(request.getInputStream(), Charsets.UTF_8)));
                log.debug("{}", TL.get());
            }
            return super.attemptAuthentication(request, response);
        } catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AuthenticationServiceException(e.getMessage(), e);
        } finally {
            TL.remove();
        }
    }

    @SneakyThrows
    @Override
    protected String obtainUsername(final HttpServletRequest request) {
        return Objects.isNull(TL.get())
                ? super.obtainUsername(request)
                : Param.valueOf(TL.get()).parseObject().getString("username");
    }

    @SneakyThrows
    @Override
    protected String obtainPassword(final HttpServletRequest request) {
        return Objects.isNull(TL.get())
                ? super.obtainPassword(request)
                : Param.valueOf(TL.get()).parseObject().getString("password");
    }
}
