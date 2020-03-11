package com.support.mvc.entity.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.support.mvc.entity.base.Item;

import javax.persistence.AttributeConverter;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 * 需要在实体类属性上添加注解：@Convert(converter = {@link ArrayItemJsonConvert}.class)
 *
 * @author 谢长春 2019/2/12
 */
public class ArrayItemJsonConvert implements AttributeConverter<Item[], String> {
    @Override
    public String convertToDatabaseColumn(final Item[] attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public Item[] convertToEntityAttribute(final String dbData) {
        return JSON.parseObject(dbData, new TypeReference<Item[]>() {
        });
    }
}
