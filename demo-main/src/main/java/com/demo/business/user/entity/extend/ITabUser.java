package com.demo.business.user.entity.extend;

import com.alibaba.fastjson.JSON;
import com.demo.business.user.entity.TabUser;
import com.demo.enums.Radio;
import com.demo.enums.RegisterSource;
import com.demo.enums.Role;
import com.mvc.entity.base.Prop;
import com.mvc.entity.base.Sorts;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.demo.business.user.entity.QTabUser.tabUser;
import static com.mvc.entity.base.Prop.Type.*;
import static com.mvc.enums.Code.ORDER_BY;

/**
 * 实体类扩展延伸
 *
 * @author 谢长春 2018/12/20
 */
public interface ITabUser {
    /**
     * 实体类所有属性名
     * 当其他地方有用到字符串引用该类属性时，应该使用该枚举定义
     */
    enum Props {
        id(LONG.build(true, "数据ID，主键自增")),
        uid(STRING.build("用户UUID，缓存和按ID查询时可使用强校验")),
        subdomain(STRING.build("子域名用户组")),
        username(STRING.build(true, "登录名")),
        password(STRING.build(true, "登录密码")),
        nickname(STRING.build("昵称")),
        phone(STRING.build("手机号")),
        email(STRING.build("邮箱")),
        role(ENUM.build("角色").setOptions(Role.comments())),
        registerSource(ENUM.build("注册渠道").setOptions(RegisterSource.comments())),
        createTime(TIMESTAMP.build("创建时间")),
        createUserId(LONG.build("创建用户ID")),
        modifyTime(TIMESTAMP.build("修改时间")),
        modifyUserId(LONG.build("修改用户ID")),
        deleted(ENUM.build("是否逻辑删除").setOptions(Radio.comments()));
        private final Prop prop;

        public Prop getProp() {
            return prop;
        }

        Props(final Prop prop) {
            prop.setName(this.name());
            this.prop = prop;
        }

        public static List<Prop> list() {
            return Stream.of(TabUser.Props.values()).map(TabUser.Props::getProp).collect(Collectors.toList());
        }

        public static List<Prop> list(final Props... props) {
            return Stream.of(props).map(TabUser.Props::getProp).collect(Collectors.toList());
        }
    }

    /**
     * 枚举：定义排序字段
     */
    enum OrderBy {
        id(tabUser.id.asc(), tabUser.id.desc()),
        // 按 id 排序可替代按创建时间排序
//        createTime(tabUser.createTime.asc(), tabUser.createTime.desc()),
        modifyTime(tabUser.modifyTime.asc(), tabUser.modifyTime.desc()),
        ;
        public final Sorts asc;
        public final Sorts desc;

        public Sorts get(final Sorts.Direction direction) {
            return Objects.equals(direction, Sorts.Direction.DESC) ? desc : asc;
        }

        /**
         * 获取所有排序字段名
         *
         * @return {@link String[]}
         */
        public static String[] names() {
            return Stream.of(TabUser.OrderBy.values()).map(Enum::name).toArray(String[]::new);
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

    /**
     * 构建排序集合
     *
     * @param sorts {@link List<Sorts.Order>}
     * @return {@link List<Sorts>}
     */
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
