package com.support.mvc.entity.base;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.support.mvc.enums.Code;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 构建分页对象
 *
 * @author 谢长春 on 2017/10/31.
 */
@Builder
public class Pager {
    public static Pager rebuild(final Pager pager) {
        return Objects.nonNull(pager) ? pager : defaultPager();
    }

    public static Pager defaultPager() {
        return Pager.builder().build().init();
    }

    /**
     * 上一页[最大|最小]的 ID 用于优化排序；谨慎使用：只能在使用 ID 排序时使用
     */
    private Long id;

    /**
     * 当前页码
     */
    private int number;
    /**
     * 每页大小
     */
    private int size;

    /**
     * 校验并初始化分页对象
     *
     * @return {@link Pager}
     */
    public Pager init() {
        number = number <= 0 ? 1 : number;
        size = size <= 0 ? 20 : size;
        return this;
    }

    public Pager assertSize() {
        if (size > 10000) throw Code.PAGE_MAX_SIZE.exception("每页最多只能查询 10000 条数据");
        return this;
    }

    /**
     * 构建 JPA 查询分页对象
     *
     * @return {@link Pageable}
     */
    public Pageable pageable() {
        return PageRequest.of(number - 1, size);
    }

    /**
     * 构建 JPA 查询分页对象
     *
     * @param sort {@link Sort}
     * @return {@link Pageable}
     */
    public Pageable pageable(final Sort sort) {
        return Objects.isNull(sort)
                ? PageRequest.of(number - 1, size)
                : PageRequest.of(number - 1, size, sort);
    }

    /**
     * QDSL 查询缩进量
     *
     * @return int
     */
    public int offset() {
        return (number - 1) * size;
    }

    /**
     * QDSL 查询大小
     *
     * @return int
     */
    public int limit() {
        return size;
    }

    /**
     * {@link Page} 转换为 {@link QueryResults}
     *
     * @param page {@link Page}
     * @return {@link QueryResults}
     */
    public <T> QueryResults<T> toQueryResults(final Page<T> page) {
        return new QueryResults<>(
                page.getContent(),
                (long) page.getSize(),
                (long) offset(),
                page.getTotalElements()
        );
    }

    /**
     * 将分页查询结果中的投影对象转换为新的分页对象
     *
     * @param results  {@link QueryResults}
     * @param function {@link Function<Tuple, T>} 将投影对象转换为 T
     * @return {@link QueryResults}
     */
    public static <T> QueryResults<T> toQueryResults(final QueryResults<Tuple> results, final Function<Tuple, T> function) {
        return new QueryResults<>(
                results.getResults().stream().map(function).collect(Collectors.toList()),
                results.getLimit(),
                results.getOffset(),
                results.getTotal()
        );
    }

    @Override
    public String toString() {
        return String.format("{\"number\":%d, \"size\":%d}", number, size);
    }
}
