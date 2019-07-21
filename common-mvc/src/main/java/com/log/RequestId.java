package com.log;

import com.utils.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 将请求唯一标志存入 ThreadLocal
 *
 * @author 谢长春 2019/6/6
 */
@Slf4j
public class RequestId {
    private static final ThreadLocal<String> TL = ThreadLocal.withInitial(() -> "");

    /**
     * 获取 ThreadLocal 值
     *
     * @return {@link String}
     */
    public static String get() {
//        if (Objects.equals("", TL.get())){
//            return RequestId.setUUID();
//        }
        return TL.get();
    }

    /**
     * 设置 ThreadLocal 值
     *
     * @return {@link String}
     */
    public static String set(final String v) {
        TL.set(v);
        return v;
    }

    /**
     * 设置 uuid 到 ThreadLocal
     *
     * @return {@link String}
     */
    public static String setUUID() {
        TL.set(Util.uuid());
        return TL.get();
    }

    /**
     * 设置 6 位随机数到 ThreadLocal
     *
     * @return {@link String}
     */
    public static String setRandomNumber() {
        TL.set(Util.random(6));
        return TL.get();
    }

    /**
     * 设置 6 位随机数 + 2 位数字或大小写字母 到 ThreadLocal
     *
     * @return {@link String}
     */
    public static String setRandomAlphanumeric() {
        TL.set(Util.random(6).concat(RandomStringUtils.randomAlphanumeric(2)));
        return TL.get();
    }

    /**
     * 移除 ThreadLocal 值
     */
    public static void remove() {
        TL.remove();
    }

    /**
     * 移除 ThreadLocal 值
     *
     * @return {@link String}
     */
    public static String getAndRemove() {
        final String v = TL.get();
        TL.remove();
        return v;
    }
}
