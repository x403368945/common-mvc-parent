package com.ccx.demo.open.auth.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.support.mvc.entity.base.Item;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * 实体：登录参数
 *
 * @author 谢长春
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@ToString(exclude = {"password"})
@Api(tags = "登录参数")
public class AuthLogin implements Serializable {
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
         * 转换为 {@link Item} 对象
         *
         * @return {@link Item}
         */
        public Item getObject() {
            return Item.builder()
                    .key(this.name())
                    .value(this.ordinal())
                    .comment(this.comment)
                    .build();
        }
    }

    /**
     * 会话模式
     */
    @ApiModelProperty(position = 1, value = "登录模式", example = "SESSION")
    private Method method = Method.SESSION;
    /**
     * 登录名，SESSION|TOKEN 模式登录名和密码必填
     */
    @ApiModelProperty(position = 2, value = "登录名，SESSION|TOKEN 模式登录名和密码必填", example = "admin")
    private String username;
    /**
     * 登录密码，SESSION|TOKEN 模式登录名和密码必填
     */
    @ApiModelProperty(position = 3, value = "登录密码，SESSION|TOKEN 模式登录名和密码必填", example = "password")
    @JSONField(serialize = false)
    private String password;
    /**
     * 手机号，CODE 模式手机号和验证码必填
     */
    @ApiModelProperty(position = 4, value = "手机号，CODE 模式手机号和验证码必填", example = "18700000000")
    private String phone;
    /**
     * 验证码，CODE 模式手机号和验证码必填
     */
    @ApiModelProperty(position = 5, value = "验证码，CODE 模式手机号和验证码必填", example = "1234")
    private String code;

    public Method getMethod() {
        if (Objects.isNull(method)) {
            return Method.SESSION; // 若未指定会话模式，则默认为SESSION模式
        }
        return method;
    }

    public static void main(String[] args) {
        System.out.println(
                "登录接口：body 参数范本:" +
                        JSON.toJSONString(
                                new AuthLogin(null, "admin", "admin", null, null)
                        )
        );
    }
}
