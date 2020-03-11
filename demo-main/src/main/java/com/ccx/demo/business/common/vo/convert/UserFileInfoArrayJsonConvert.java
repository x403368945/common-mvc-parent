package com.ccx.demo.business.common.vo.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ccx.demo.business.common.vo.UserFileInfo;

import javax.persistence.AttributeConverter;

import static com.alibaba.fastjson.serializer.SerializerFeature.IgnoreNonFieldGetter;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 * 需要在实体类属性上添加注解：@Convert(converter = {@link UserFileInfoArrayJsonConvert}.class)
 *
 * @author 谢长春 2019/2/12
 */
public class UserFileInfoArrayJsonConvert implements AttributeConverter<UserFileInfo[], String> {
    @Override
    public String convertToDatabaseColumn(final UserFileInfo[] attribute) {
        return JSON.toJSONString(attribute, IgnoreNonFieldGetter);
    }

    @Override
    public UserFileInfo[] convertToEntityAttribute(final String dbData) {
        return JSON.parseObject(dbData, new TypeReference<UserFileInfo[]>() {
        });
    }
}
