package com.ccx.demo.business.common.vo.convert;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.common.vo.UserFileInfo;

import javax.persistence.AttributeConverter;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 * 需要在实体类属性上添加注解：@Convert(converter = {@link UserFileInfoJsonConvert}.class)
 *
 * @author 谢长春 2019/2/12
 */
public class UserFileInfoJsonConvert implements AttributeConverter<UserFileInfo, String> {
    @Override
    public String convertToDatabaseColumn(final UserFileInfo attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public UserFileInfo convertToEntityAttribute(final String dbData) {
        return JSON.parseObject(dbData, UserFileInfo.class);
    }
}
