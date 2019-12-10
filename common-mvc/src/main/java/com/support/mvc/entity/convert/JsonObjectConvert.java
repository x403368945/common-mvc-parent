package com.support.mvc.entity.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.persistence.AttributeConverter;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 * 需要在实体类属性上添加注解：@Convert(converter = {@link JsonObjectConvert}.class)
 *
 * @author 谢长春 2019/2/12
 */
public class JsonObjectConvert implements AttributeConverter<JSONObject, String> {
    @Override
    public String convertToDatabaseColumn(final JSONObject attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public JSONObject convertToEntityAttribute(final String dbData) {
        return JSON.parseObject(dbData);
    }
}
