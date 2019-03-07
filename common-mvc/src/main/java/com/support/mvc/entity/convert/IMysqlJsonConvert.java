package com.support.mvc.entity.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import javax.persistence.AttributeConverter;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 *
 * @author 谢长春 2019/2/12
 */
public interface IMysqlJsonConvert<T> extends AttributeConverter<T, String> {
    @Override
    default String convertToDatabaseColumn(final T o) {
        return JSON.toJSONString(o);
    }

    @Override
    default T convertToEntityAttribute(final String s) {
        return JSON.parseObject(s, new TypeReference<T>() {
        });
    }
}
