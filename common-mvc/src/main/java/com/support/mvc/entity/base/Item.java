package com.support.mvc.entity.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;

/**
 * 通用简单对象
 *
 * @author 谢长春 2019-7-9
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Accessors(chain = true)
@JSONType(orders = {"key", "value", "checked", "comment", "childs"})
public class Item {
    /**
     * 一般用于枚举 Enum::name()
     */
    private String key;
    /**
     * 值，一般用于枚举 Enum::ordinal()
     */
    private Object value;
    /**
     * 是否被选中，true：选中状态，false未选中状态
     */
    private Boolean checked;
    /**
     * 文本，一般用于枚举 comment
     */
    private String comment;
    /**
     * 子节点
     */
    private List<Item> childs;

    /**
     * 将 value 转换为 int，value = null -> 0
     *
     * @return {@link Integer}
     */
    public int intValue() {
        if (value instanceof Number) return ((Number) value).intValue();
        return 0;
    }

    /**
     * 将 value 转换为 int，value = null -> defaultValue = null -> 0
     *
     * @param defaultValue {@link Number} 当 value = null 时指定默认值，当 defaultValue 也为 null 时，返回 0
     * @return {@link Integer}
     */
    public int intValue(final Number defaultValue) {
        if (value instanceof Number) return ((Number) value).intValue();
        return Objects.isNull(defaultValue) ? 0 : defaultValue.intValue();
    }

    /**
     * 将 value 转换为字符串；value = null -> null
     *
     * @return {@link String}
     */
    public String stringValue() {
        return Objects.toString(value, null);
    }

    /**
     * 将 value 转换为字符串，value = null -> defaultValue = null -> null
     *
     * @param defaultValue {@link String} 当 value = null 时指定默认值，当 defaultValue 也为 null 时，返回 null
     * @return {@link String}
     */
    public String stringValue(final String defaultValue) {
        return Objects.toString(value, defaultValue);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
