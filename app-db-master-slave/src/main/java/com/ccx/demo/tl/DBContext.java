package com.ccx.demo.tl;

import com.ccx.demo.enums.DBRoute;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

import static com.ccx.demo.enums.DBRoute.*;

/**
 * ThreadLocal 将数据源设置到每个线程上下文中
 *
 * @author 谢长春 2019/6/9
 */
@Slf4j
public class DBContext {

    private static final ThreadLocal<DBRoute> TL = ThreadLocal.withInitial(() -> MASTER); // 默认为主库
    private static final AtomicInteger counter = new AtomicInteger(0);

    /**
     * 获取 ThreadLocal 值
     *
     * @return {@link DBRoute}
     */
    public static DBRoute get() {
        return TL.get();
    }

    /**
     * 设置 ThreadLocal 值
     *
     * @return {@link DBRoute}
     */
    public static DBRoute set(final DBRoute v) {
        TL.set(v);
        log.debug("数据源：【{}】", TL.get().name());
        return v;
    }

//    /**
//     * 移除 ThreadLocal 值
//     */
//    public static DBRoute remove() {
//        final DBRoute v = TL.get();
//        TL.remove();
//        return v;
//    }

    /**
     * 移除 ThreadLocal 值
     */
    public static void remove() {
        TL.remove();
    }

    /**
     * 切换到主库
     */
    public static void master() {
        set(MASTER);
    }

    /**
     * 切换到从库：轮询策略
     */
    public static void slave() {
        final int index = counter.getAndIncrement() % 2;
        if (counter.get() > 9999) {
            counter.set(0);
        }
        set((index == 0) ? SECOND : THIRD);
    }
}
