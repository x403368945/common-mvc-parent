package com.support.mvc.entity.convert;

import com.alibaba.fastjson.JSON;
import com.support.mvc.entity.base.Item;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 * 需要在实体类属性上添加注解：@Convert(converter = {@link ListItemJsonConvert}.class)
 *
 * @author 谢长春 2019/2/12
 * @deprecated List 类型 QueryDSL 不支持 update 方法，请使用 {@link ArrayItemJsonConvert}
 */
@Deprecated
public class ListItemJsonConvert implements AttributeConverter<List<Item>, String> {
    @Override
    public String convertToDatabaseColumn(final List<Item> attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<Item> convertToEntityAttribute(final String dbData) {
        return JSON.parseArray(dbData, Item.class);
    }
}
