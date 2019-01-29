package com.boot.security.enums;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

/**
 * session 中的字段名
 *
 *
 * @author 谢长春 on 2017/11/23.
 */
public enum Session {
    code("图片验证码对应的值"),
    user("用户信息"),
    token("请求头的 token 字段名"),
    ;
    final String comment;

    Session(final String comment) {
        this.comment = comment;
    }

    public Optional<Object> get(final HttpSession session) {
        if (Objects.isNull(session)) {
            return Optional.empty();
        }
        return Optional.ofNullable(session.getAttribute(this.name()));
    }

    public Optional<String> getString(final HttpSession session) {
        if (Objects.isNull(session)) {
            return Optional.empty();
        }
        return Optional.ofNullable(session.getAttribute(this.name())).map(Objects::toString);
    }
}
