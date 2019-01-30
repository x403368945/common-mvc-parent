package com.mvc.demo.business.common.entity;

import com.mvc.demo.config.init.AppConfig.Path;
import com.support.mvc.enums.Code;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Html 模板
 *
 *
 * @author 谢长春 on 2018/1/14.
 */
@Slf4j
public class Html {
    /**
     * 邮件验证码 html 模板格式化，返回格式化后的html
     *
     * @param name String 用户昵称
     * @param code  String 验证码
     * @return String html
     */
    public static String emailCode(final String name, final String code) {
        Objects.requireNonNull(name, "参数【name】是必须的");
        Objects.requireNonNull(code, "参数【code】是必须的");
        return Path.HTML.read("email-code.html")
                .replace("<%name%>", name) // 用户昵称
                .replace("<%code%>", code) // 验证码
                ;
    }
    /**
     * 重置密码 html 模板格式化，返回格式化后的html
     *
     * @param name String 用户昵称
     * @param url  String 重置链接
     * @return String html
     */
    public static String resetPassword(final String name, final String url) {
        Objects.requireNonNull(name, "参数【name】是必须的");
        Objects.requireNonNull(url, "参数【url】是必须的");
        return Path.HTML.read("reset-password.html")
                .replace("<%name%>", name) // 用户昵称
                .replace("<%url%>", url) // 激活url
                ;
    }
    /**
     * 消息提醒html
     *
     * @param code Code 消息状态
     * @param message String 消息内容
     * @return String html
     */
    public static String message(Code code, final String message) {
        Objects.requireNonNull(message, "参数【message】是必须的");
        return Path.HTML.read("message.html")
                .replace("<%code%>", code.name()) // 消息编码
                .replace("<%message%>", message) // 消息内容
                ;
    }
}
