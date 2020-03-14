package com.ccx.demo.open.auth.service;

import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.entity.TabUserLogin;
import com.ccx.demo.business.user.service.UserLoginService;
import com.ccx.demo.business.user.service.UserService;
import com.support.mvc.enums.Code;
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
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * 登录时要生成token，完成Spring Security认证，然后返回token给客户端
 * 注册时将用户密码用BCrypt加密，写入用户角色
 *
 * @author 谢长春
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    //    private MailService mailService;
//    private UserCache userCache;
//    private PhoneCode phoneCode;
    private final UserLoginService userLoginService;
//    private EmailActivate emailActivate;
//    private ResetPassword resetPasswordService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
//            TabUser obj = JSON.parseObject(jsonText, TabUser.class);
//            Asserts.notEmpty(obj.getdomain(), "字段【domain】不能为空");
        Assert.hasText(username, "参数【username】不能为空");
        return userService.findUser(username)
                .map(TabUser::loadUserDetail)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在：".concat(username)));
    }

    /**
     * 用户名密码登录
     *
     * @param username String 用户名
     * @param password String 密码
     * @return TabUser
     */
    @Validated
    public TabUser login(@NotBlank final String username,
                         @NotBlank final String password,
                         @NotBlank final String ip
    ) {
        try {
            // 构造 username、password 登录认证token
            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 授权认证
            final Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final TabUser user = (TabUser) authentication.getPrincipal();
            // 保存登录记录
            userLoginService.save(TabUserLogin.builder().userId(user.getId()).ip(ip).build(), user.getId());
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

//    @Override
//    public TabUser regist(final UserRegist regist) throws Exception {
//        { // 手机号注册
////            if (!JUnit.get()) { // 校验手机验证码
////                if (!Cache.getInstance().checkPhoneCode(regist.getPhone(), regist.getCode()))
////                    throw Code.PHONE_CODE_ERROR.exception("手机验证码不匹配");
////            }
//        }
//        final TabUser user = userService.save(
//                TabUser.builder()
////                        .username(regist.getPhone())
////                        .phone(regist.getPhone())
//                        .username(regist.getEmail())
//                        .email(regist.getEmail())
//                        .password(regist.getPassword())
//                        .nickname(Util.isNotEmpty(regist.getNickname()) ? regist.getNickname() : regist.getEmail().substring(0, regist.getEmail().lastIndexOf("@")))
//                        .registerSource(Objects.isNull(regist.getSource()) ? RegisterSource.System : regist.getSource())
//                        .role(Role.ROLE_USER)
//                        .build()
//                , App.AdminUserId.get() // 新用户注册，使用管理员账号ID作为创建者和修改者
//        );
//        return user;
//    }
//
//    @Override
//    public String activate(final String uuid) {
//        try {
//            Optional<String> userId = emailActivate.getUserId(uuid);
//            if (userId.isPresent()) {
//                userService.activate(userId.get());
//                return "redirect:/"; // 激活成功
//            } else { // 激活链接已过期
//                return "redirect:/activate-expired.html";
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            return "redirect:/activate-failure.html"; // 激活失败
//        }
//    }

//    @Override
//    public void forgetPassword(final String email) throws Exception {
//        Optional<TabUser> optional = userService.findByUsername(email);
//        Asserts.isTrue(optional.isPresent(), String.format("邮箱【%s】未注册", email));
//        { // 邮箱注册，发送账户激活邮件
//            try {
//                mailService.sendResetPassword(optional.get(),
//                        resetPasswordService.getUrl(optional.get()) // 获取重置链接
//                );
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//                throw Code.CHECK_EMAIL.exception("邮件发送失败");
//            }
//        }
//    }

}
