package com.mvc.entity;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * 所有需要更新时间戳的实体类，都可以实现该接口；默认会取 modifyTime 的时间，如果实体类的时间戳 字段不是 modifyTime， 需要重写 getModifyTime() 指向时间戳字段
 *
 *
 * @author 谢长春 2017-9-26
 */
public interface ITimestamp {

    Timestamp getModifyTime();

    default long getTimestamp() {
        return Optional.ofNullable(getModifyTime()).map(Timestamp::getTime).orElse(0L);
    }

}