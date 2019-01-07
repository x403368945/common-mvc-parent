package com.mvc.entity.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.utils.IJson;
import com.utils.util.Num;
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
 * @author 谢长春 2016-11-23
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Accessors(chain = true)
@JSONType(orders = {"value", "label", "checked", "comment", "childs"})
public class Item implements IJson {
    /**
     * 值
     */
    private Object value;
    /**
     * 文本
     */
    private String label;
    /**
     * 是否被选中，true：选中状态，false未选中状态
     */
    private Boolean checked;
    /**
     * 备注
     */
    private String comment;
    /**
     * 子节点
     */
    private List<Item> childs;

    public int intValue() {
        return Num.of(value).intValue();
    }

    public int intValue(final Number defaultValue) {
        return Num.of(Objects.toString(value), defaultValue).intValue();
    }

    public String stringValue() {
        return Objects.toString(value);
    }

    public String stringValue(final String defaultValue) {
        return Objects.toString(value, defaultValue);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
