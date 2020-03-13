package com.support.mvc.entity;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.support.mvc.dao.IRepository;
import com.support.mvc.dao.ISearchRepository;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Sorts;
import com.utils.util.Then;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 动态查询条件规范接口
 * 用于实现构建 {@link ISearchRepository} 需要的查询条件
 *
 * @author 谢长春 2018-1-12
 */
public interface IWhere<U, W> {

    /**
     * 动态 update 语句规范
     * 用于实现 {@link IRepository#update(Object, Long, Object)} 动态更新参数
     *
     * @param update {@link Then}
     * @return {@link Then}
     */
    default Then<U> update(final U update) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【update(final U update)】未实现动态更新方法"));
    }

    /**
     * <pre>
     * 构建 QueryDSL 查询条件，用于执行
     * {@link ISearchRepository#findList(Object)}
     * {@link ISearchRepository#findPage(Object, Pager)}
     * 构建查询顺序规则：
     * 带索引的字段
     * 一次能排除最多数据的字段
     * 绝对相等的字段
     * 区间查询字段
     * like查询字段，无必要情况尽量避免字段前后都带 % 查询 %name%
     *
     * @return {@link QdslWhere}
     */
    W where();

    /**
     * 配置默认排序字段
     *
     * @return {@link List<Sorts>}
     */
    default List<Sorts> defaultSorts() {
        return Collections.emptyList();
    }

    /**
     * 排序字段集合
     *
     * @return {@link List<Sorts>}
     */
    default List<Sorts> parseSorts() {
        return null;
    }

    /**
     * 排序参数构建：QueryDSL 查询模式；QueryDSL 式构建排序对象返回 null 则会抛出异常
     *
     * @return {@link OrderSpecifier[]}
     */
    default OrderSpecifier<?>[] buildQdslSorts() {
        final List<Sorts> sorts = parseSorts();
        if (Objects.isNull(sorts) || sorts.isEmpty()) {
            // 按默认字段排序
            return defaultSorts().stream().map(Sorts::qdsl).collect(Collectors.toList()).toArray(new OrderSpecifier[]{});
        }
        // 按指定字段排序
        return sorts.stream().map(Sorts::qdsl).collect(Collectors.toList()).toArray(new OrderSpecifier[]{});
    }

    /**
     * 排序参数构建：Spring JPA 查询模式；JPA 模式构建排序对象返回 null 表示不排序
     *
     * @return {@link Sort}
     */
    default Sort buildJpaSort() {
        final List<Sorts> sorts = parseSorts();
        if (Objects.isNull(sorts) || sorts.isEmpty()) { // 按默认字段排序
            return Sort.by(defaultSorts().stream().map(Sorts::jpa).collect(Collectors.toList()));
        }
        // 按指定字段排序
        return Sort.by(sorts.stream().map(Sorts::jpa).collect(Collectors.toList()));
    }

    /**
     * QueryDSL 查询条件
     */
    class QdslWhere {
        private QdslWhere() {
        }

        /**
         * 查询条件集合
         */
        private final List<BooleanExpression> expressions = new ArrayList<>();

        /**
         * 初始化 where 构建对象
         *
         * @return {@link QdslWhere}
         */
        public static QdslWhere of() {
            return new QdslWhere();
        }

        /**
         * 使用一个查询条件初始化 where 构建对象
         *
         * @param predicate {@link BooleanExpression} 查询表达式
         * @return {@link QdslWhere}
         */
        public static QdslWhere of(final BooleanExpression predicate) {
            return new QdslWhere().and(predicate);
        }

        /**
         * where 条件拼接
         *
         * @param or {@link Or}
         * @return {@link QdslWhere}
         */
        public QdslWhere and(final Or or) {
            Objects.requireNonNull(or, "参数【or】是必须的");
            if (or.notEmpty()) {
                expressions.add(or.toPredicate());
            }
            return this;
        }

        /**
         * where 条件拼接
         *
         * @param predicate {@link BooleanExpression}
         * @return {@link QdslWhere}
         */
        public QdslWhere and(final BooleanExpression predicate) {
            Objects.requireNonNull(predicate, "参数【predicate】是必须的");
            expressions.add(predicate);
            return this;
        }

        /**
         * where 条件拼接
         *
         * @param value    Object value非空时，执行supplier.value()获得查询条件
         * @param supplier {@link Supplier<BooleanExpression>}
         * @return {@link QdslWhere}
         */
        public QdslWhere and(final Object value, final Supplier<BooleanExpression> supplier) {
            return and(Objects.nonNull(value), supplier);
        }

        /**
         * where 条件拼接
         *
         * @param hasTrue  boolean hasTrue为true时，执行supplier.get()获得查询条件
         * @param supplier {@link Supplier<BooleanExpression>}
         * @return {@link QdslWhere}
         */
        public QdslWhere and(final boolean hasTrue, final Supplier<BooleanExpression> supplier) {
            if (hasTrue) {
                expressions.add(supplier.get()); // Optional.ofNullable(supplier.get()).map(v -> expressions.add(v));
            }
            return this;
        }

        /**
         * where 条件拼接
         *
         * @param value    Object value非空时，执行supplier.value()获得查询条件
         * @param supplier {@link Supplier<BooleanExpression>}
         * @return {@link QdslWhere}
         */
        public QdslWhere andIfNull(final Object value, final Supplier<BooleanExpression> supplier) {
            return and(Objects.isNull(value), supplier);
        }

        /**
         * where 条件拼接，同 {@link IWhere.QdslWhere}#{@link IWhere.QdslWhere#and(Object, Supplier)}
         *
         * @param value    Object value非空时，执行supplier.value()获得查询条件
         * @param supplier {@link Supplier<BooleanExpression>}
         * @return {@link QdslWhere}
         */
        public QdslWhere andIfNonNull(final Object value, final Supplier<BooleanExpression> supplier) {
            return and(value, supplier);
        }

//        /**
//         * where 条件拼接
//         *
//         * @param value    String value非空时，执行supplier.value()获得查询条件
//         * @param supplier {@link Supplier<BooleanExpression>}
//         * @return {@link QdslWhere}
//         */
//        public QdslWhere andIfBlank(final String value, final Supplier<BooleanExpression> supplier) {
//            return and(Optional.ofNullable(value).filter(v -> Objects.equals("", value.trim())).isPresent(), supplier);
//        }

        /**
         * where 条件拼接
         *
         * @param value    String value非空，且非空字符串时，执行supplier.value()获得查询条件
         * @param supplier {@link Supplier<BooleanExpression>}
         * @return {@link QdslWhere}
         */
        public QdslWhere andIfNonBlank(final String value, final Supplier<BooleanExpression> supplier) {
            return and(StringUtils.isNotBlank(value), supplier);
        }

        /**
         * where 条件拼接
         *
         * @param collection {@link Collection} value非空且集合大小必须大于 0 ，执行supplier.value()获得查询条件
         * @param supplier   {@link Supplier<BooleanExpression>}
         * @return {@link QdslWhere}
         */
        public QdslWhere andIfNonEmpty(final Collection<?> collection, final Supplier<BooleanExpression> supplier) {
            return and(CollectionUtils.isNotEmpty(collection), supplier);
        }

        public QdslWhere andIfNonEmpty(final Object[] objects, final Supplier<BooleanExpression> supplier) {
            return and(ArrayUtils.isNotEmpty(objects), supplier);
        }

        public boolean isEmpty() {
            return 0 == expressions.size();
        }

        public boolean notEmpty() {
            return !isEmpty();
        }

        /**
         * 获取查询条件集合
         *
         * @return {@link List<BooleanExpression>}
         */
        public List<BooleanExpression> get() {
            return expressions;
        }

        /**
         * 获取查询条件数组
         *
         * @return {@link BooleanExpression[]}
         */
        public BooleanExpression[] toArray() {
            return expressions.toArray(new BooleanExpression[]{});
        }

        /**
         * 归集 and 条件集合到一个对象
         *
         * @return {@link BooleanExpression}
         */
        public BooleanExpression toPredicate() {
            if (isEmpty()) {
                return null;
            }
            return (1 == expressions.size())
                    ? expressions.get(0)
                    : expressions.stream().reduce(BooleanExpression::and).orElse(null);
        }

        /**
         * 构建 or 条件
         */
        public final static class Or {
            /**
             * or 条件集合
             */
            private final List<BooleanExpression> expressions = new ArrayList<>();

            /**
             * 初始化 where 构建对象
             *
             * @return {@link Or}
             */
            public static Or of() {
                return new Or();
            }

            /**
             * 使用一个查询条件初始化 or 构建对象
             *
             * @param expression {@link BooleanExpression} 查询表达式
             * @return {@link Or}
             */
            public static Or of(final BooleanExpression expression) {
                return new Or().or(true, () -> expression);
            }

            /**
             * or 条件拼接
             *
             * @param value    Object value非空时，执行supplier.get()获得查询条件
             * @param supplier {@link Supplier<BooleanExpression>}
             * @return {@link Or}
             */
            public Or or(final Object value, final Supplier<BooleanExpression> supplier) {
                Objects.requireNonNull(supplier, "参数【supplier】是必须的");
                return or(Objects.nonNull(value), supplier);
            }

            /**
             * or 条件拼接
             *
             * @param hasTrue  boolean hasTrue为true时，执行supplier.get()获得查询条件
             * @param supplier {@link Supplier<BooleanExpression>}
             * @return {@link Or}
             */
            public Or or(final boolean hasTrue, final Supplier<BooleanExpression> supplier) {
                Objects.requireNonNull(supplier, "参数【supplier】是必须的");
                if (hasTrue) {
                    expressions.add(supplier.get()); // Optional.ofNullable(supplier.get()).map(v -> expressions.add(v));
                }
                return this;
            }

            /**
             * or 条件拼接
             *
             * @param expression {@link BooleanExpression}
             * @return {@link Or}
             */
            public Or or(final BooleanExpression expression) {
                expressions.add(expression);
                return this;
            }

            public boolean isEmpty() {
                return 0 == expressions.size();
            }

            public boolean notEmpty() {
                return !isEmpty();
            }

            /**
             * 获取 or 条件集合
             *
             * @return {@link List<BooleanExpression>}
             */
            public List<BooleanExpression> get() {
                return expressions;
            }

            /**
             * 归集 or 条件集合到一个对象
             *
             * @return {@link Predicate}
             */
            public BooleanExpression toPredicate() {
                if (isEmpty()) {
                    return null;
                }
                return (1 == expressions.size())
                        ? expressions.get(0)
                        : expressions.stream().reduce(BooleanExpression::or).orElse(null);
            }
        }
    }

}