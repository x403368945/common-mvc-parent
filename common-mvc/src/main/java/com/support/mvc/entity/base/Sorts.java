package com.support.mvc.entity.base;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
         *
         * @param direction {@link Sorts.Direction}
         * @return {@link Sorts}
         */
        Sorts get(final Sorts.Direction direction);
    }

    /**
     * 排序方向
     */
    public enum Direction {
        ASC("正序"), DESC("倒序");
        /**
         * 枚举属性说明
         */
        final String comment;

        Direction(String comment) {
            this.comment = comment;
        }

        @Deprecated
        public static String[] names() {
            return Stream.of(Direction.values()).map(Enum::name).toArray(String[]::new);
        }

        /**
         * 转换为 {@link Item} 对象
         *
         * @return {@link Item}
         */
        public Item getObject() {
            return Item.builder()
                    .key(this.name())
                    .value(this.ordinal())
                    .comment(this.comment)
                    .build();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    @Accessors(fluent = false)
    @ApiModel(description = "通用排序参数接收对象")
    public static class Order implements Serializable {
        private static final long serialVersionUID = 8760879633278119365L;
        /**
         * 排序字段名
         */
        @ApiModelProperty(required = true, value = "排序字段名，可以通过实体类的 OrderBy 枚举查看所有支持排序的字段")
        private String name;
        /**
         * 排序方向
         */
        @Builder.Default
        @ApiModelProperty(position = 1, value = "排序方向，com.support.mvc.entity.base.Sorts.Direction", example = "ASC")
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
