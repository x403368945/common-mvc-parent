package com.ccx.demo.business.example.entity.convert;

import com.alibaba.fastjson.JSON;
import com.support.mvc.entity.base.Item;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 *
 * @author 谢长春 2019/2/12
 */
public class ListItemJsonConvert implements AttributeConverter<List<Item>, String> {
    @Override
    public String convertToDatabaseColumn(final List<Item> o) {
        return JSON.toJSONString(o);
    }

    @Override
    public List<Item> convertToEntityAttribute(final String s) {
        return JSON.parseArray(s, Item.class);
    }
}
