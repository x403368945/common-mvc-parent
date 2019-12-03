package com.support.mvc.entity.base;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * 查询排序对象
 *
 * @author 谢长春 on 2017/12/21.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Accessors(fluent = true)
public class Sorts implements Serializable {

    private static final long serialVersionUID = -3920676158297540091L;

    /**
     * OrderBy 枚举规范接口
     */
    public interface IOrderBy {
        /**
         * 按排序方向获取字段对应的排序对象
         * @param direction {@link Sorts.Direction}
         * @return {@link Sorts}
         */
        Sorts get(final Sorts.Direction direction);
    }

    /**
     * 排序方向
     */
    public enum Direction {
        ASC, DESC;

        public static String[] names() {
            return Stream.of(Direction.values()).map(Enum::name).toArray(String[]::new);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    @Accessors(fluent = false)
    public static class Order implements Serializable {
        private static final long serialVersionUID = 8760879633278119365L;
        /**
         * 排序字段名
         */
        private String name;
        /**
         * 排序方向
         */
        @Builder.Default
        private Direction direction = Direction.ASC;
    }

    /**
     * 构造正序排序对象
     *
     * @param qdsl {@link ComparableExpressionBase} QueryDSL模式
     * @param jpa  {@link Enum} JPA模式使用 OrderBy 枚举名作为排除字段
     * @return {@link Sorts}
     */
    public static Sorts asc(final ComparableExpressionBase<?> qdsl, final Enum<?> jpa) {
        return Sorts.builder().qdsl(qdsl.asc()).jpa(Sort.Order.asc(jpa.name())).build();
    }

    /**
     * 构造正序排序对象
     *
     * @param qdsl {@link ComparableExpressionBase} QueryDSL模式
     * @param jpa  {@link Enum} JPA模式使用 OrderBy 枚举名作为排除字段
     * @return {@link Sorts}
     */
    public static Sorts desc(final ComparableExpressionBase<?> qdsl, final Enum<?> jpa) {
        return Sorts.builder().qdsl(qdsl.desc()).jpa(Sort.Order.desc(jpa.name())).build();
    }

    /**
     * QueryDSL 查询模式排序对象
     */
    private OrderSpecifier<?> qdsl;
    /**
     * Spring JPA 查询模式排序对象
     */
    private Sort.Order jpa;
}
