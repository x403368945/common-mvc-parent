package com.ccx.demo.business.example.enums;


import com.support.mvc.entity.base.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 枚举：Demo数据状态
 *
 *
 * @author 谢长春 2018/12/17
 */
public enum DemoStatus {
    _NONE("无效"),
    WATING("等待中"),
    RUNNING("执行中"),
    SUCCESS("成功"),
    FAILURE("失败"),
    ;
    /**
     * 枚举属性说明
     */
    final String comment;

    DemoStatus(final String comment) {
        this.comment = comment;
    }


    /**
     * 构建选项集合
     *
     * @return {@link List<Item>}
     */
    public static List<Item> options() {
        return Stream.of(DemoStatus.values())
                .map(item -> Item.builder().value(item.name()).label(item.comment).build())
                .collect(Collectors.toList());
    }

    /**
     * 构建选项注释集合
     *
     * @return {@link Map<String, String>}
     */
    public static Map<String, String> comments() {
        return Stream.of(DemoStatus.values()).collect(Collectors.toMap(
                DemoStatus::name,
                o -> o.comment,
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new
        ));
    }
}
