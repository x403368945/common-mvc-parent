package com.mvc.demo.enums;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户注册渠道
 *
 *
 * @author 谢长春 2017年7月4日 下午5:19:05
 */
public enum RegisterSource {
    /**
     * 管理员指定
     */
    SYSTEM("管理员指定"),
    /**
     * PC-浏览器注册
     */
    WEB("PC-浏览器注册"),
    /**
     * Android App注册
     */
    ANDROID("Android App注册"),
    /**
     * IOS App注册
     */
    IOS("IOS App注册"),
    /**
     * 微信注册
     */
    WECHAT("微信注册");
    /**
     * 枚举属性说明
     */
    final String comment;

    RegisterSource(String comment) {
        this.comment = comment;
    }

    /**
     * 构建选项注释集合
     *
     * @return {@link Map <String, String>}
     */
    public static Map<String, String> comments() {
        return Stream.of(RegisterSource.values()).collect(Collectors.toMap(
                RegisterSource::name,
                o -> o.comment,
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new
        ));
    }
}