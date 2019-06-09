package com.ccx.security.enums;

import com.support.mvc.entity.base.Item;
import com.utils.util.Util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 选择状态，只有两种状态，YES or NO
 *
 *
 * @author 谢长春 2017年7月4日 下午5:19:22
 */
public enum Radio {
    /**
     * 否|未删除|未读|待处理|未验证|未完成|不支持|未确认|无效|未过期|暂停
     */
    NO(" 否|未删除|未读|待处理|未验证|未完成|不支持|未确认|无效|未过期|暂停"),
    /**
     * 是|已删除|已读|已处理|已验证|已完成|  支持|已确认|有效|已过期|启动
     */
    YES("是|已删除|已读|已处理|已验证|已完成|  支持|已确认|有效|已过期|启动");
    /**
     * 枚举属性说明
     */
    final String comment;

    Radio(String comment) {
        this.comment = comment;
    }

    public int value() {
        return this.ordinal();
    }

    public static Radio valueOf(final Integer value) {
        return Util.isEmpty(value) ? null : Radio.values()[value];
    }

    /**
     * 构建 YES or NO 选项
     *
     * @param yes {@link String} YES 对应的文本
     * @param no  {@link String} NO 对应的文本
     * @return {@link List<Item>}
     */
    public static List<Item> options(final String yes, final String no) {
        return Arrays.asList(
                Item.builder().value(Radio.YES.name()).label(yes).comment(Radio.YES.comment).build(),
                Item.builder().value(Radio.NO.name()).label(no).comment(Radio.NO.comment).build()
        );
    }

    /**
     * 构建选项注释集合
     *
     * @return {@link Map<String, String>}
     */
    public static Map<String, String> comments() {
        return Stream.of(Radio.values()).collect(Collectors.toMap(
                Radio::name,
                o -> o.comment,
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new
        ));
    }
}
