
package com.support.mvc.entity.convert;

import com.alibaba.fastjson.JSON;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 * 需要在实体类属性上添加注解：@Convert(converter = {@link ListLongJsonConvert}.class)
 *
 * @author 谢长春 2019/2/12
 * @deprecated List 类型 QueryDSL 不支持 update 方法，请使用 {@link ArrayLongJsonConvert}
 */
@Deprecated
public class ListLongJsonConvert implements AttributeConverter<List<Long>, String> {
    @Override
    public String convertToDatabaseColumn(final List<Long> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<Long> convertToEntityAttribute(final String dbData) {
        return JSON.parseArray(dbData, Long.class);
    }
}
