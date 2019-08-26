package com.ccx.demo.business.example.entity.convert;

import com.alibaba.fastjson.JSON;
import com.support.mvc.entity.base.Item;

import javax.persistence.AttributeConverter;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 *
 * @author 谢长春 2019/2/12
 */
//@Converter(autoApply = true)
public class ItemJsonConvert implements AttributeConverter<Item, String> {
    @Override
    public String convertToDatabaseColumn(final Item o) {
        return JSON.toJSONString(o);
    }

    @Override
    public Item convertToEntityAttribute(final String s) {
        return JSON.parseObject(s, Item.class);
    }
}