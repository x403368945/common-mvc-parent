package com.ccx.demo.enums;


import com.support.mvc.entity.base.Item;

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