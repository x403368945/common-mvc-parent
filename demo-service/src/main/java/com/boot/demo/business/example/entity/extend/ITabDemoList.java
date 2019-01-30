package com.boot.demo.business.example.entity.extend;

import com.alibaba.fastjson.JSON;
import com.boot.demo.business.example.entity.QTabDemoList;
import com.boot.demo.business.example.entity.TabDemoList;
import com.boot.demo.business.example.enums.DemoStatus;
import com.boot.demo.enums.Radio;
import com.support.mvc.entity.base.Prop;
import com.support.mvc.entity.base.Sorts;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.support.mvc.entity.base.Prop.*;
import static com.support.mvc.entity.base.Prop.Type.*;
import static com.support.mvc.enums.Code.ORDER_BY;

/**
 * 扩展 {@link TabDemoList}；可以通过接口扩展的形式减少实体类的代码量
 *
 *
 * @author 谢长春 2018/12/20
 */
public interface ITabDemoList {
    /**
     * 实体类所有属性名
     * 当其他地方有用到字符串引用该类属性时，应该使用该枚举定义
     */
    enum Props {
        id(LONG.build(true, "数据ID，主键自增")),
        uid(STRING.build(true, "数据UUID，缓存和按ID查询时可使用强校验")),
        name(STRING.build(true, "名称")),
        content(STRING.build("内容")),
        amount(DOUBLE.build("金额")),
        status(ENUM.build(true, "状态").setOptions(DemoStatus.comments())),
        createTime(TIMESTAMP.build("创建时间")),
        createUserId(LONG.build("创建用户ID")),
        createUserName(STRING.build("创建用户昵称")),
        modifyTime(TIMESTAMP.build("修改时间")),
        modifyUserId(LONG.build("修改用户ID")),
        modifyUserName(STRING.build("修改用户昵称")),
        deleted(ENUM.build("是否逻辑删除").setOptions(Radio.comments())),
        timestamp(LONG.build("数据最后一次更新时间戳")),
        amountRange(RANGE_NUM.apply("金额查询区间")),
        createTimeRange(RANGE_DATE.apply("创建时间查询区间")),
        sorts(SORTS.apply(OrderBy.names())),
        ;
        private final Prop prop;

        public Prop getProp() {
            return prop;
        }

        Props(final Prop prop) {
            prop.setName(this.name());
            this.prop = prop;
        }

        public static List<Prop> list() {
            return Stream.of(Props.values()).map(Props::getProp).collect(Collectors.toList());
        }
    }

    /**
     * 枚举：定义排序字段
     */
    enum OrderBy {
        // 按 id 排序可替代按创建时间排序
        id(QTabDemoList.tabDemoList.id.asc(), QTabDemoList.tabDemoList.id.desc()),
        //		uid(tabDemoList.uid.asc(), tabDemoList.uid.desc()),
//		name(tabDemoList.name.asc(), tabDemoList.name.desc()),
//		content(tabDemoList.content.asc(), tabDemoList.content.desc()),
//		amount(tabDemoList.amount.asc(), tabDemoList.amount.desc()),
//		status(tabDemoList.status.asc(), tabDemoList.status.desc()),
//		createTime(tabDemoList.createTime.asc(), tabDemoList.createTime.desc()),
//		createUserId(tabDemoList.createUserId.asc(), tabDemoList.createUserId.desc()),
//		createUserName(tabDemoList.createUserName.asc(), tabDemoList.createUserName.desc()),
        modifyTime(QTabDemoList.tabDemoList.modifyTime.asc(), QTabDemoList.tabDemoList.modifyTime.desc()),
//		modifyUserId(tabDemoList.modifyUserId.asc(), tabDemoList.modifyUserId.desc()),
//		modifyUserName(tabDemoList.modifyUserName.asc(), tabDemoList.modifyUserName.desc()),
//		deleted(tabDemoList.deleted.asc(), tabDemoList.deleted.desc())
        ;
        public final Sorts asc;
        public final Sorts desc;

        public Sorts get(final Sorts.Direction direction) {
            return Objects.equals(direction, Sorts.Direction.ASC) ? asc : desc;
        }

        /**
         * 获取所有排序字段名
         *
         * @return {@link String[]}
         */
        public static String[] names() {
            return Stream.of(OrderBy.values()).map(Enum::name).toArray(String[]::new);
        }

        OrderBy(OrderSpecifier qdslAsc, OrderSpecifier qdsldesc) {
            asc = Sorts.builder()
                    .qdsl(qdslAsc)
                    .jpa(Sort.Order.asc(this.name()))
                    .build();
            desc = Sorts.builder()
                    .qdsl(qdsldesc)
                    .jpa(Sort.Order.desc(this.name()))
                    .build();
        }
    }

    default List<Sorts> buildSorts(final List<Sorts.Order> sorts) {
        try {
            return Optional.ofNullable(sorts)
                    .map(list -> list.stream()
                            .map(by -> OrderBy.valueOf(by.getName()).get(by.getDirection()))
                            .collect(Collectors.toList())
                    )
                    .orElse(Collections.singletonList(OrderBy.id.desc)); // 若排序字段为空，这里可以设置默认按 id 倒序
        } catch (Exception e) {
            throw ORDER_BY.exception("排序字段可选范围：".concat(JSON.toJSONString(OrderBy.names())));
        }
    }
}