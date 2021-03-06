package com.support.mvc.entity.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import javax.persistence.AttributeConverter;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 * 需要在实体类属性上添加注解：@Convert(converter = {@link JsonArrayConvert}.class)
 *
 * @author 谢长春 2019/2/12
 */
public class JsonArrayConvert implements AttributeConverter<JSONArray, String> {
    @Override
    public String convertToDatabaseColumn(final JSONArray attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public JSONArray convertToEntityAttribute(final String dbData) {
        return JSON.parseArray(dbData);
    }
}
