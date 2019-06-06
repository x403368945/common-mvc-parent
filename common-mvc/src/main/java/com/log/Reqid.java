package com.log;

/**
 * 将请求唯一标志存入 ThreadLocal
 *
 * @author 谢长春 2019/6/6
 */
public class Reqid {
    private static final ThreadLocal<String> TL = ThreadLocal.withInitial(() -> "");

    /**
     * 获取 ThreadLocal 值
     *
     * @return {@link String}
     */
    public static String get() {
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
     * 移除 ThreadLocal 值
     */
    public static String remove() {
        final String v = TL.get();
        TL.remove();
        return v;
    }
}
