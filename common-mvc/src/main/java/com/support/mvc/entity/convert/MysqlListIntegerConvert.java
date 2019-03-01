package com.support.mvc.entity.convert;

import java.util.List;

/**
 * 支撑 mysql 原生 JSON 数据类型与实体类属性的映射；
 * 需要在实体类属性上添加注解：@Convert(converter = {@link MysqlListIntegerConvert}.class)
 *
 * @author 谢长春 2019/2/12
 */
public class MysqlListIntegerConvert implements IMysqlJsonConvert<List<Integer>> {
}
