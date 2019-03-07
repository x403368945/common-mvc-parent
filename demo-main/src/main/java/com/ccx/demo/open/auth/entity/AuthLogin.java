package com.ccx.demo.open.auth.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.support.mvc.entity.base.Prop;
import com.utils.util.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.support.mvc.entity.base.Prop.Type.ENUM;
import static com.support.mvc.entity.base.Prop.Type.STRING;

/**
 * 实体：登录参数
 *
 * @author 谢长春
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class AuthLogin implements Serializable {
    /**
     * 实体类所有属性名
     */
    public enum Props {
        method(ENUM.build("登录模式").setOptions(Method.comments())),
        username(STRING.build("SESSION|TOKEN登录模式，必填")),
        password(STRING.build("SESSION|TOKEN登录模式，必填")),
        phone(STRING.build("CODE登录模式，必填")),
        code(STRING.build("CODE登录模式，必填")),
        ;
        private final Prop prop;

        public Prop getProp() {
            return prop;
        }

        Props(final Prop prop) {
            prop.setName(this.name());
            this.prop = prop;
        }

        public static List<Prop> list() {
            return Stream.of(Props.values()).map(Props::getProp).collect(Collectors.toList());
        }
    }

    /**
     * 会话模式分类
     */
    public enum Method {
        SESSION("用户名密码登录，以会话模式实现，用户信息存储在session中，有效期默认为30分钟"),
        TOKEN("户名密码登录，以TOKEN建立会话，用户信息存储在头部的token中，有效期默认为30天"),
        CODE("手机验证码模式登录，以TOKEN建立会话，用户信息存储在头部的token中，有效期默认为永久有效"),
        ;
        /**
         * 枚举属性说明
         */
        final String comment;

        Method(final String comment) {
            this.comment = comment;
        }

        /**
         * 构建选项注释集合
         *
         * @return {@link Map <String, String>}
         */
        public static Map<String, String> comments() {
            return Stream.of(Method.values()).collect(Collectors.toMap(
                    Method::name,
                    o -> o.comment,
                    (u, v) -> {
                        throw new IllegalStateException(String.format("Duplicate key %s", u));
                    },
                    LinkedHashMap::new
            ));
        }
    }

    /**
     * 会话模式
     */
    private Method method = Method.SESSION;
    /**
     * 登录名，SESSION|TOKEN 模式登录名和密码必填
     */
    private String username;
    /**
     * 登录密码，SESSION|TOKEN 模式登录名和密码必填
     */
    @JSONField(serialize = false)
    private String password;
    /**
     * 手机号，CODE 模式手机号和验证码必填
     */
    private String phone;
    /**
     * 验证码，CODE 模式手机号和验证码必填
     */
    private String code;

    public Method getMethod() {
        if (Objects.isNull(method)) {
            return Method.SESSION; // 若未指定会话模式，则默认为SESSION模式
        }
        return method;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static void main(String[] args) {
        System.out.println(
                "登录接口：body 参数范本:" +
                        JSON.toJSONString(
                                Maps.by("json", new AuthLogin(null, "admin", "admin", null, null))
                        )
        );
    }
}
