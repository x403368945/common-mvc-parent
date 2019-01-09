package com.mvc.entity.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.utils.IJson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.mvc.entity.base.Prop.Type.*;

/**
 * 数据库字段说明
 *
 *
 * @author 谢长春 2018-10-10
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Accessors(chain = true)
@JSONType(orders = {"name", "type", "required", "comment", "props"})
public class Prop implements IJson {
    private static final List<Prop> RANGE_NUM_CHILDS = Arrays.asList(
            Prop.builder().name("mix").comment("最小值，这里兼容[SHORT|INT|LONG|FLOAT|DOUBLE]，所以不指定 type 属性").build(),
            Prop.builder().name("max").comment("最大值，这里兼容[SHORT|INT|LONG|FLOAT|DOUBLE]，所以不指定 type 属性").build()
    );
    public static final Function<String, Prop> RANGE_NUM = comment -> Prop.builder().type(OBJECT).comment(comment).props(RANGE_NUM_CHILDS).build();

    private static final List<Prop> RANGE_DATE_CHILDS = Arrays.asList(
            Prop.builder().name("begin").type(TIMESTAMP).comment("开始时间").build(),
            Prop.builder().name("end").type(TIMESTAMP).comment("截止时间").build()
    );
    public static final Function<String, Prop> RANGE_DATE = comment -> Prop.builder().type(OBJECT).comment(comment).props(RANGE_DATE_CHILDS).build();

    public static final Function<Object, Prop> SORTS = arrs -> Prop.builder()
            .name("sorts")
            .type(ARRAY)
            .comment("排序字段集合")
            .props(Collections.singletonList(Prop.builder()
                    .type(OBJECT)
                    .comment("排序字段对象")
                    .props(Arrays.asList(
                            Prop.builder().name("name").comment("可选的排序字段").type(ENUM).required(true).options(arrs).build(),
                            Prop.builder().name("direction").type(ENUM).required(true).options(Sorts.Direction.names()).build()
                    ))
                    .build()
            ))
            .build()
    ;

    /**
     * 字段类型
     */
    public enum Type {
        SHORT,
        INT,
        LONG,
        DOUBLE,
        STRING,
        TIMESTAMP,
        ENUM,
        ARRAY,
        OBJECT,
        JSON_STRING,
        ;

        public Prop build(final String comment) {
            return Prop.builder().type(this).comment(comment).build();
        }

        public Prop build(final boolean required, final String comment) {
            return Prop.builder().type(this).required(required).comment(comment).build();
        }

        public PropBuilder builder(final String comment) {
            return Prop.builder().type(this).comment(comment);
        }

        public PropBuilder builder(final boolean required, final String comment) {
            return Prop.builder().type(this).required(required).comment(comment);
        }
    }

    /**
     * 字段名
     */
    private String name;
    /**
     * 数据类型
     */
    private Type type;
    /**
     * 是否必填
     */
    private boolean required;
    /**
     * 备注
     */
    private String comment;
    /**
     * 选项
     */
    private Object options;
    /**
     * 子节点
     */
    private List<Prop> props;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

//    public enum Props {
//        id(LONG.build(true, "数据ID，主键自增")),
//        uid(STRING.build(true, "数据UUID，缓存和按ID查询时可使用强校验")),
//        name(STRING.build(true, "名称")),
//        createTime(TIMESTAMP.build("创建时间")),
//        createUserId(LONG.build("创建用户ID")),
//        modifyTime(TIMESTAMP.build("修改时间")),
//        modifyUserId(LONG.build("修改用户ID")),
//        deleted(ENUM.build(Radio.comments())),
//        timestamp(TIMESTAMP.build("数据最后一次更新时间戳")),
//        ageRange(RANGE_NUM.apply("年龄查询区间")),
//        createTimeRange(RANGE_DATE.apply("创建时间查询区间")),
////        sorts(VERSION.apply(DemoList.OrderBy.names())),
//        ;
//        private final Prop extend;
//
//        public Prop getProp() {
//            return extend;
//        }
//
//        Props(final Prop extend) {
//            extend.setName(this.name());
//            this.extend = extend;
//        }
//
//        public static List<Prop> list() {
//            return Stream.of(Props.values()).map(Props::getProp).collect(Collectors.toList());
//        }
//    }


    public static void main(String[] args) {
        System.out.println(
                JSON.toJSONString(
                        Arrays.asList(
                                Prop.builder().name("id").type(LONG).required(true).comment("数据ID，自增").build(),
                                Prop.builder().name("uid").type(STRING).required(true).comment("数据UUID，缓存和按ID查询时可使用强校验").build(),
                                Prop.builder().name("name").type(STRING).required(true).comment("姓名").build(),
                                Prop.builder().name("address").type(STRING).comment("地址").build(),
                                Prop.builder().name("ageRange").type(OBJECT).comment("年龄查询区间")
                                        .props(Arrays.asList(
                                                Prop.builder().name("mix").type(SHORT).comment("最小值").build(),
                                                Prop.builder().name("max").type(SHORT).comment("最大值").build()
                                        ))
                                        .build(),
                                Prop.builder().name("createTimeRange").type(OBJECT).comment("创建时间查询区间")
                                        .props(Arrays.asList(
                                                Prop.builder().name("begin").type(TIMESTAMP).comment("开始日期").build(),
                                                Prop.builder().name("end").type(TIMESTAMP).comment("截止日期").build()
                                        ))
                                        .build(),
                                SORTS.apply(Arrays.asList("name", "age"))

                        )
                        , SerializerFeature.PrettyFormat
                )
        );
    }
}
