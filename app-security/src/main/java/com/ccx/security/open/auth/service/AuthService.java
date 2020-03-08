package com.ccx.security.open.auth.service;

import com.ccx.security.business.user.entity.TabUser;
import com.ccx.security.enums.Bool;
import com.ccx.security.enums.Role;
import com.mysema.commons.lang.Assert;
import com.support.mvc.enums.Code;
import com.utils.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 登录时要生成token，完成Spring Security认证，然后返回token给客户端
 * 注册时将用户密码用BCrypt加密，并写入用户角色
 *
 * @author 谢长春
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private static final List<TabUser> USERS = Arrays.asList(
            TabUser.builder().id(1L).uid(Util.uuid32()).username("admin").role(Role.ROLE_ADMIN)
                    .password(new BCryptPasswordEncoder().encode("admin")).deleted(Bool.NO).build(),
            TabUser.builder().id(2L).uid(Util.uuid32()).username("user").role(Role.ROLE_USER)
                    .password(new BCryptPasswordEncoder().encode("111111")).deleted(Bool.NO).build()
    );

    private final AuthenticationManager authenticationManager;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Assert.hasText(username, "参数【username】不能为空");
        return USERS.stream()
                .filter(user -> Objects.equals(user.getUsername(), username))
                .map(TabUser::loadUserDetail)
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在：".concat(username)));
    }

    /**
     * 用户名密码登录
     *
     * @param username String 用户名
     * @param password String 密码
     * @return TabUser
     */
    public TabUser login(final String username, final String password) {
        Assert.hasText(username, "字段【username】不能为空");
        Assert.hasText(password, "字段【password】不能为空");
        try {
            // 构造 username、password 登录认证token
            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 授权认证
            final Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final TabUser user = (TabUser) authentication.getPrincipal();
//            // 保存登录记录
//            userLoginService.save(TabUserLogin.builder().userId(user.getId()).build(), user.getId());
            return user;
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
            if (e instanceof AccountExpiredException) {
                throw Code.USER_EXPIRED.exception("账户已过期");
            }
            if (e instanceof DisabledException) {
                throw Code.USER_DISABLED.exception("账户已禁用");
            }
            throw Code.USER_PWD.exception("用户名或密码错误");
        }
    }

}
