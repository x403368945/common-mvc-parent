package com.ccx.demo.enums;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.support.mvc.entity.base.Item;

import java.util.Objects;

/**
 * 选择状态，只有两种状态，YES or NO
 *
 * @author 谢长春 2017年7月4日 下午5:19:22
 */
public enum Bool {
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

    Bool(String comment) {
        this.comment = comment;
    }

    public int value() {
        return this.ordinal();
    }

    public static Bool valueOf(final Integer value) {
        return Objects.isNull(value) ? null : Bool.values()[value];
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(Bool.values()));
        {
            SerializeConfig serializeConfig = new SerializeConfig();
            serializeConfig.configEnumAsJavaBean(Bool.class);
            System.out.println(JSON.toJSONString(Bool.values(), serializeConfig));
        }
        try {
            SerializeConfig serializeConfig = new SerializeConfig();
            Class<Enum> clazz = (Class<Enum>) Class.forName("com.ccx.demo.enums.Bool");
            serializeConfig.configEnumAsJavaBean(clazz);
            System.out.println(JSON.toJSONString(Bool.values(), serializeConfig));

            System.out.println(JSON.toJSONString(clazz.getEnumConstants()));
            for (Enum constant : clazz.getEnumConstants()) {
                Item item = (Item) clazz.getMethod("getObject").invoke(constant);
                System.out.println(JSON.toJSONString(item));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
