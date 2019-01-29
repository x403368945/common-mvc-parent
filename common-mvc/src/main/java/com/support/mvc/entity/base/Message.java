package com.support.mvc.entity.base;

import com.alibaba.fastjson.annotation.JSONType;
import com.utils.IJson;
import com.utils.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 事件消息对象
 *
 *
 * @author 谢长春 on 2017/11/16.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Accessors(chain = true)
@JSONType(orders = {"event", "message", "extras"})
public class Message implements IJson {

    /**
     * 可选的事件属性
     */
    private Object event;
    /**
     * 异常消息内容
     */
    private String message;
    /**
     * 扩展属性
     */
    private Map<String, Object> extras;

    /**
     * 添加附加属性
     *
     * @param key   {@link String}
     * @param value {@link Object}
     * @return {@link Message}
     */
    public Message addExtras(String key, Object value) {
        if (Util.isEmpty(this.extras)) {
            this.extras = new LinkedHashMap<>(3);
        }
        extras.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return json();
    }
}
