
package com.support.mvc.entity.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import javax.persistence.AttributeConverter;
import java.util.Set;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 * 需要在实体类属性上添加注解：@Convert(converter = {@link MysqlSetLongConvert}.class)
 *
 * @author 谢长春 2019/2/12
 */
public class MysqlSetLongConvert implements AttributeConverter<Set<Long>, String> {
    @Override
    public String convertToDatabaseColumn(final Set<Long> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public Set<Long> convertToEntityAttribute(final String dbData) {
        return JSON.parseObject(dbData, new TypeReference<Set<Long>>() {
        });
    }
}
