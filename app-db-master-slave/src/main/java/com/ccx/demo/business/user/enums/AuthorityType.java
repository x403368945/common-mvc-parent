package com.ccx.demo.business.user.enums;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 * 枚举：权限类型
 *
 * @author 谢长春 2019/8/27
 */
public enum AuthorityType {
    /**
     * 标记首页主菜单：控制菜单显示或隐藏
     */
    MENU("菜单"),
    /**
     * 控制按钮操作权限：必定会请求后台指定的接口；同 LOAD 类似，BUTTON 是标记为按钮触发的请求
     */
    BUTTON("按钮"),
    /**
     * 控制页面加载调用接口权限：必定会请求后台指定的接口；部分页面加载时调用的 API 比较多，且调用并不是按钮触发的，可以用 LOAD 权限区分
     */
    LOAD("加载"),
    /**
     * 控制其他动作权限：纯粹的控制页面展示逻辑，不会与后台接口交互，可以用来控制页面某个区块不显示，或者控制某个并不请求后台接口的路由地址权限
     */
    ACTION("动作"),
    ;
    final String comment;

    AuthorityType(final String comment) {
        this.comment = comment;
    }

    /**
     * 构建选项注释集合
     *
     * @return {@link Map <String, String>}
     */
    public static Map<String, String> comments() {
        return Stream.of(AuthorityType.values()).collect(Collectors.toMap(
                AuthorityType::name,
                o -> o.comment,
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new
        ));
    }
}
