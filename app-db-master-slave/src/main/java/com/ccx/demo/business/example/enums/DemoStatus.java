package com.ccx.demo.business.example.enums;


import com.support.mvc.entity.base.Item;

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
