package com.support.mvc.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义异常: 删除异常
 *
 * @author 谢长春 2016-11-23
 */
@Slf4j
public class DeleteRowsException extends RuntimeException {
    /**
     * 断言是否抛出删除异常，数据变更行数必须为1
     *
     * @param rows long 影响行数
     */
    public static void asserts(final long rows) {
        if (1 != rows) {
            throw new DeleteRowsException(String.format("删除失败，影响行数:%d", rows));
        }
    }

    public static void asserts(final int rows) {
        asserts((long) rows);
    }

    /**
     * 当数据变更行数与批量集合数量不一致时，打印警告日志
     *
     * @param rows   long 影响行数
     * @param length long 集合大小
     */
    public static void warn(final long rows, final long length) {
        if (rows != length) {
            log.warn(String.format("批量操作警告，影响行数【%d != %d】集合大小", rows, length));
        }
    }

    public static void warn(final int rows, final int length) {
        warn((long) rows, (long) length);
    }

//    /**
//     * 断言是否为批量删除
//     * 断言是否抛出批量删除异常，数据变更行数必须大于0
//     *
//     * @param rows long 影响行数
//     */
//    public static void batch(long rows) {
//        if (rows <= 0) {
//            throw new DeleteRowsException(String.format("批量删除失败，影响行数:%d", rows));
//        }
//    }
//
//    public static void batch(int rows) {
//        batch((long) rows);
//    }


    public DeleteRowsException() {
        super();
    }

    public DeleteRowsException(String msg) {
        super(msg);
    }
//
//    public DeleteRowsException(String msg, Throwable cause) {
//        super(msg, cause);
//    }
//
//    public DeleteRowsException(Throwable cause) {
//        super(cause);
//    }
}
