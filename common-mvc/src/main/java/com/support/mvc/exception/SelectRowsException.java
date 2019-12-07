package com.support.mvc.exception;

import java.util.Collection;
import java.util.Objects;

/**
 * 自定义异常: 数据查询异常
 *
 * @author 谢长春 2019-9-5
 */
public class SelectRowsException extends RuntimeException {

    /**
     * 断言是否抛出数据查询异常，数据查询行数不能大于1
     *
     * @param collection {@link Collection} 查询结果集
     */
    public static void asserts(final Collection<?> collection) {
        if (collection.size() > 1) {
            throw new SelectRowsException(String.format("数据查询失败，预期查询行数不大于1，实际查询行数:%d", collection.size()));
        }
    }

    /**
     * 断言是否抛出数据查询异常，数据查询行数必须等于 1 ； 当查询结果为 null、空集合、集合数量大于 1 都会抛出异常
     *
     * @param collection {@link Collection} 查询结果集
     */
    public static void assertOne(final Collection<?> collection) {
        if (Objects.isNull(collection) || collection.isEmpty()) {
            throw new SelectRowsException("数据查询失败，预期查询行数等于1，实际查询行数:0");
        }
        if (collection.size() > 1) {
            throw new SelectRowsException(String.format("数据查询失败，预期查询行数等于1，实际查询行数:%d", collection.size()));
        }
    }

    public SelectRowsException() {
        super();
    }

    public SelectRowsException(String msg) {
        super(msg);
    }
}
