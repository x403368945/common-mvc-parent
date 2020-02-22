package com.support.mvc.entity.base;

import com.alibaba.fastjson.annotation.JSONType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "通用下拉选项")
@JSONType(orders = {"key", "value", "checked", "comment", "childs"})
public class Item {
    /**
     * 一般用于枚举 Enum::name()
     */
    @ApiModelProperty(value = "选项名称，枚举项名称，用于前端交互")
    private String key;
    /**
     * 值，一般用于枚举 Enum::ordinal()
     */
    @ApiModelProperty(position = 1, value = "选项序号，枚举项序号，用于数据库存储")
    private Object value;
    /**
     * 是否被选中，true：选中状态，false未选中状态
     */
    @ApiModelProperty(position = 2, value = "是否被选中，true：选中状态，false：未选中状态")
    private Boolean checked;
    /**
     * 文本，一般用于枚举 comment
     */
    @ApiModelProperty(position = 3, value = "选项说明，枚举项说明，用于前端展示")
    private String comment;
    /**
     * 是否废弃
     */
    @ApiModelProperty(position = 4, value = "该选项是否已废弃，true：是，false：否")
    private Boolean deprecated;
    /**
     * 子节点
     */
    @ApiModelProperty(position = 5, value = "子节点集合")
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

}
