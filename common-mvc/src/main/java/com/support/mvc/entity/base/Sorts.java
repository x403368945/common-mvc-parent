package com.support.mvc.entity.base;

import com.querydsl.core.types.OrderSpecifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

/**
 * 查询排序对象
 *
 *
 * @author 谢长春 on 2017/12/21.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Accessors(fluent = true)
public class Sorts {

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
    public static class Order {
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
     * QueryDSL 查询模式排序对象
     */
    private OrderSpecifier qdsl;
    /**
     * Spring JPA 查询模式排序对象
     */
    private Sort.Order jpa;
}
