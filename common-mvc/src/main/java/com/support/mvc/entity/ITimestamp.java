package com.support.mvc.entity;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * 所有需要更新时间戳的实体类，都可以实现该接口；默认会取 updateTime 的时间，如果实体类的时间戳 字段不是 updateTime， 需要重写 getUpdateTime() 指向时间戳字段
 *
 *
 * @author 谢长春 2017-9-26
 */
public interface ITimestamp {

    Timestamp getUpdateTime();

    default long getTimestamp() {
        return Optional.ofNullable(getUpdateTime()).map(Timestamp::getTime).orElse(0L);
    }

}