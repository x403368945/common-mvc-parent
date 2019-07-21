package com.support.mvc.actions;

import com.support.mvc.entity.base.Message;
import com.utils.util.Logs;
import com.utils.util.Util;

import java.util.Map;
import java.util.Objects;

import static com.support.mvc.actions.ICallback.Event.*;

/**
 * 回调接口
 *
 * @author 谢长春 2018-10-11
 */
public interface ICallback {
    /**
     * 回调事件
     */
    enum Event {
        START,
        INFO,
        WARN,
        ERROR,
        SUCCESS,
        END
    }

    /**
     * 设置日志记录处理
     *
     * @param logger {@link Logs}
     */
    void setLogger(final Logs logger);

    /**
     * 消息回调
     *
     * @param message {@link Message}
     * @return {@link Message}
     */
    Message sendMessage(final Message message);

    /**
     * 消息回调
     *
     * @param message {@link String} 消息内容，可以用 {} 占位，按顺序替换 args 中的值，替换时使用Objects.toString()
     * @param args    {@link Object[]} 消息内容中的占位符对应参数
     * @return {@link Message}
     */
    default Message start(final String message, final Object... args) {
        return sendMessage(START, Util.format(message, args));
    }

    /**
     * 消息回调
     *
     * @param message {@link String} 消息内容，可以用 {} 占位，按顺序替换 args 中的值，替换时使用Objects.toString()
     * @param args    {@link Object[]} 消息内容中的占位符对应参数
     * @return {@link Message}
     */
    default Message info(final String message, final Object... args) {
        return sendMessage(INFO, Util.format(message, args));
    }

    /**
     * 消息回调
     *
     * @param message {@link String} 消息内容，可以用 {} 占位，按顺序替换 args 中的值，替换时使用Objects.toString()
     * @param args    {@link Object[]} 消息内容中的占位符对应参数
     * @return {@link Message}
     */
    default Message warn(final String message, final Object... args) {
        return sendMessage(WARN, Util.format(message, args));
    }

    /**
     * 消息回调
     *
     * @param message {@link String} 消息内容，可以用 {} 占位，按顺序替换 args 中的值，替换时使用Objects.toString()
     * @param args    {@link Object[]} 消息内容中的占位符对应参数
     * @return {@link Message}
     */
    default Message error(final String message, final Object... args) {
        return sendMessage(ERROR, Util.format(message, args));
    }

    /**
     * 消息回调
     *
     * @param message {@link String} 消息内容，可以用 {} 占位，按顺序替换 args 中的值，替换时使用Objects.toString()
     * @param args    {@link Object[]} 消息内容中的占位符对应参数
     * @return {@link Message}
     */
    default Message success(final String message, final Object... args) {
        return sendMessage(SUCCESS, Util.format(message, args));
    }

    /**
     * 消息回调
     *
     * @param message {@link String} 消息内容，可以用 {} 占位，按顺序替换 args 中的值，替换时使用Objects.toString()
     * @param key     {@link String} 扩展属性key
     * @param value   {@link Object} 扩展属性值
     * @param args    {@link Object[]} 消息内容中的占位符对应参数
     * @return {@link Message}
     */
    default Message success(final String message, final String key, final Object value, final Object... args) {
        return sendMessage(SUCCESS, Util.format(message, args), key, value);
    }

    /**
     * 消息回调
     *
     * @param message {@link String} 消息内容，可以用 {} 占位，按顺序替换 args 中的值，替换时使用Objects.toString()
     * @param args    {@link Object[]} 消息内容中的占位符对应参数
     * @return {@link Message}
     */
    default Message end(final String message, final Object... args) {
        return sendMessage(END, Util.format(message, args));
    }

    /**
     * 消息回调
     *
     * @param event   {@link Object} 事件
     * @param message {@link String} 消息内容，可以用 {} 占位，按顺序替换 args 中的值，替换时使用Objects.toString()
     * @param args    {@link Object[]} 消息内容中的占位符对应参数
     * @return {@link Message}
     */
    default Message sendMessage(final Object event, final String message, final Object... args) {
        return sendMessage(
                Message.builder()
                        .event(event)
                        .message(Util.format(message, args))
                        .build()
        );
    }

    /**
     * 消息回调
     *
     * @param event   {@link Object} 事件
     * @param message {@link String} 消息内容，可以用 {} 占位，按顺序替换 args 中的值，替换时使用Objects.toString()
     * @param extras  {@link Map<String, Object>} 附加参数
     * @param args    {@link Object[]} 消息内容中的占位符对应参数
     * @return {@link Message}
     */
    default Message sendMessage(final Object event, final String message, final Map<String, Object> extras, final Object... args) {
        return sendMessage(
                Message.builder()
                        .event(event)
                        .message(Util.format(message, args))
                        .extras(extras)
                        .build()
        );
    }

    /**
     * 消息回调
     *
     * @param event   {@link Object} 事件
     * @param message {@link String} 消息内容，可以用 {} 占位，按顺序替换 args 中的值，替换时使用Objects.toString()
     * @param key     {@link String} 扩展属性key
     * @param value   {@link Object} 扩展属性值
     * @param args    {@link Object[]} 消息内容中的占位符对应参数
     * @return {@link Message}
     */
    default Message sendMessage(final Object event, final String message, final String key, final Object value, final Object... args) {
        return sendMessage(Message.builder()
                .event(event)
                .message(Util.format(message, args))
                .build()
                .addExtras(key, value)
        );
    }

    abstract class AbstractCallback implements ICallback {
        protected Logs logger = null;

        @Override
        public void setLogger(Logs logger) {
            this.logger = logger;
        }

        protected void log(final Message message) {
            if (Objects.nonNull(logger)) logger.d(message.toString());
        }
    }

}