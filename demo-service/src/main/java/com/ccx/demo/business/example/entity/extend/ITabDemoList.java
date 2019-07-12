package com.ccx.demo.business.example.entity.extend;

import com.ccx.demo.business.example.entity.TabDemoList;
import com.ccx.demo.business.example.enums.DemoStatus;
import com.ccx.demo.enums.Radio;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.support.mvc.entity.base.Prop;
import com.support.mvc.entity.base.Sorts;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ccx.demo.business.example.entity.QTabDemoList.tabDemoList;
import static com.support.mvc.entity.base.Prop.*;
import static com.support.mvc.entity.base.Prop.Type.*;

/**
 * 扩展 {@link TabDemoList}；可以通过接口扩展的形式减少实体类的代码量
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
        id(tabDemoList.id),
        //		uid(tabDemoList.uid),
//		name(tabDemoList.name),
//		content(tabDemoList.content),
//		amount(tabDemoList.amount),
//		status(tabDemoList.status),
//		createTime(tabDemoList.createTime),
//		createUserId(tabDemoList.createUserId),
//		createUserName(tabDemoList.createUserName),
        modifyTime(tabDemoList.modifyTime),
//		modifyUserId(tabDemoList.modifyUserId),
//		modifyUserName(tabDemoList.modifyUserName),
//		deleted(tabDemoList.deleted)
        ;
        public final Sorts asc;
        public final Sorts desc;

        public Sorts get(final Sorts.Direction direction) {
            return Objects.equals(direction, Sorts.Direction.ASC) ? asc : desc;
        }
        public Sorts.Order asc() {
            return Sorts.Order.builder().name(this.name()).direction(Sorts.Direction.ASC).build();
        }
        public Sorts.Order desc() {
            return Sorts.Order.builder().name(this.name()).direction(Sorts.Direction.DESC).build();
        }

        /**
         * 获取所有排序字段名
         *
         * @return {@link String[]}
         */
        public static String[] names() {
            return Stream.of(OrderBy.values()).map(Enum::name).toArray(String[]::new);
        }

        OrderBy(ComparableExpressionBase qdsl) {
            asc = Sorts.asc(qdsl, this);
            desc = Sorts.desc(qdsl, this);
        }
    }
}
