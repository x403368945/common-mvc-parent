package com.mvc.entity.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONType;
import com.mvc.enums.Code;
import com.mysema.commons.lang.Assert;
import com.utils.util.Maps;
import com.utils.util.Util;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 参数对象
 *
 * @author 谢长春 2017-9-20
 */
@Slf4j
@NoArgsConstructor
@Data
@Accessors(chain = true)
@JSONType(orders = {"required", "empty", "array", "json"})
public class Param implements Serializable {
    private static final long serialVersionUID = 6441278102384460156L;

    /**
     * 将 jsonText 反序列化成 Param 对象
     *
     * @param jsonText String
     * @return {@link Param}
     */
    public static Param valueOf(final String jsonText) {
        if (Objects.isNull(jsonText)) {
            return new Param();
        }
        return JSON.parseObject(jsonText, Param.class);
    }

    /**
     * 注意与上面 valueOf 不同；
     * 实例化 Param 将 json 设置到属性中
     *
     * @param json String
     * @return {@link Param}
     */
    public static Param of(final String json) {
        return new Param().setJson(json);
    }

    /**
     * 注意与上面 valueOf 不同；
     * 实例化 Param 将 object 转成 json字符串之后设置到属性中
     *
     * @param object String
     * @return {@link Param}
     */
    public static Param of(final Object object) {
        return new Param().setJson(JSON.toJSONString(object));
    }

    /**
     * 当 Param 为 null时抛出异常
     *
     * @param param {@link Param}
     */
    public static Param of(final Param param) {
        Assert.notNull(param, "参数集合为空，必须指定参数:{json:【{对象}|[数组]】}");
        return param;
    }

//    /**
//     * {@link com.mvc.web.IController#search(TabUser, int, Param)}
//     * {@link com.mvc.web.IController#page(TabUser, int, int, int, Param)}
//     * 方法中的 Param 反序列化时走这个构造函数
//     *
//     * @param json {@link String}
//     */
//    public Param(final String json) {
//        super();
//        this.json = json;
//    }
//    /**
//     * {@link com.mvc.web.IController#search(TabUser, int, Param)}
//     * {@link com.mvc.web.IController#page(TabUser, int, int, int, Param)}
//     * 方法中的 Param 反序列化时走这个构造函数
//     *
//     * @param json {@link String}
//     */
//    public Param(final Object json) {
//        super();
//        this.json = JSON.toJSONString(json);
//    }

    /**
     * 前端参数集合
     */
    private String json;

    /**
     * 判断 json 是否为空或者为一个空集合
     *
     * @return boolean true：是，false：否
     */
    public boolean isEmpty() {
        return Util.isEmpty(json) || "{}".equals(json) || "[]".equals(json) || "[{}]".equals(json);
    }

    /**
     * 判断 json 是否非空且为数组
     *
     * @return boolean true：是，false：否
     */
    public boolean isArray() {
        return Util.isNotEmpty(json) && json.startsWith("[") && json.endsWith("]");
    }

    /**
     * 判断 json 不能为空
     *
     * @return {@link Param}
     */
    public Param required() {
        Assert.isFalse(isEmpty(), "请求参数【json】是必须的：{json:{对象}||[数组]}");
        return this;
    }

    /**
     * 判断 json 必须是数组，否则抛出异常
     *
     * @return {@link Param}
     */
    public Param hasArray() {
        Assert.isTrue(isArray(), "请求参数【json】必须为数组：{json:[]}");
        return this;
    }

    /**
     * 将 json 转换为 JSONObject, 若json为空，则返回一个空的JSONObject实例<br>
     * json为空或json格式不标准时抛出 Code.VALIDATED.exception("json解析失败") 异常
     *
     * @return JSONObject
     */
    public JSONObject parseObject() {
        try {
            return JSON.parseObject(Optional.ofNullable(json).orElse("{}"));
        } catch (Exception e) {
            throw Code.VALIDATED.exception("请求参数解析失败，原因：JSON格式错误，或字段数据类型不匹配", e);
        }
    }

    /**
     * 将json转换为泛型指定的类对象， 若json为空则返回一个空的 <T> 实例<br>
     * json为空或json格式不标准时抛出 Code.VALIDATED.exception("json解析失败") 异常
     *
     * @param clazz Class<T>
     * @return <T> 实例
     */
    public <T> T parseObject(final Class<T> clazz) {
        try {
            return JSON.parseObject(Optional.ofNullable(json).orElse("{}"), clazz);
        } catch (Exception e) {
            throw Code.VALIDATED.exception("请求参数解析失败，原因：JSON格式错误，或字段数据类型不匹配", e);
        }
    }

    /**
     * 将json转换为泛型指定的泛型类型对象，若json为空则返回 null<br>
     * json为空或json格式不标准时抛出 Code.VALIDATED.exception("json解析失败") 异常
     *
     * @param type 泛型类型对象
     * @return <T> 实例
     */
    public <T> T parseObject(final TypeReference<T> type) {
        try {
            return JSON.parseObject(Optional.ofNullable(json).orElse("{}"), type.getType());
        } catch (Exception e) {
            throw Code.VALIDATED.exception("请求参数解析失败，原因：JSON格式错误，或字段数据类型不匹配", e);
        }
    }

    /**
     * 将 json 转换为 JSONArray, 若json为空，则返回一个空的JSONArray实例<br>
     * json为空或json格式不标准时抛出 Code.VALIDATED.exception("json解析失败") 异常
     *
     * @param clazz Class<T> 泛型类型对象
     */
    public <T> List<T> parseArray(final Class<T> clazz) {
        try {
            return JSON.parseArray(Optional.ofNullable(json).orElse("[]"), clazz);
        } catch (Exception e) {
            throw Code.VALIDATED.exception("请求参数解析失败，原因：JSON格式错误，或字段数据类型不匹配", e);
        }
    }

    /**
     * 将 json 转换为 JSONArray, 若json为空，则返回一个空的JSONArray实例<br>
     * json为空或json格式不标准时抛出 Code.VALIDATED.exception("json解析失败") 异常
     *
     * @return JSONArray
     */
    public JSONArray parseArray() {
        try {
            return JSON.parseArray(Optional.ofNullable(json).orElse("{}"));
        } catch (Exception e) {
            throw Code.VALIDATED.exception("请求参数解析失败，原因：JSON格式错误，或字段数据类型不匹配", e);
        }
    }

    @Override
    public String toString() {
        return Maps.ofSO()
                .put("empty", this.isEmpty())
                .put("array", this.isArray())
                .put("json", isEmpty() ? null : isArray() ? JSON.parseArray(this.json) : JSON.parseObject(this.json))
                .json();
    }

//    /**
//     * 警告：此断言异常将会抛参数错误到前端，若异常不是前端传的参数错误，请使用 Assertss 断言类
//     * 参数断言，断言不满足则抛出 Code.VALIDATED 异常；
//     *
//     * @author 谢长春 on 2017/10/26.
//     */
//    public static class Asserts {
//        /**
//         * 校验boolean是否为true；若为 false 否则抛出 Code.VALIDATED 异常
//         *
//         * @param value   boolean
//         * @param message String 异常消息内容
//         */
//        public static void isTrue(final boolean value, final String message) {
//            if (!value) {
//                throw Code.VALIDATED.exception(message);
//            }
//        }
//
//        /**
//         * 校验boolean是否为false；若为 true 则抛出 Code.VALIDATED 异常
//         *
//         * @param value   boolean
//         * @param message String 异常消息内容
//         */
//        public static void isFalse(final boolean value, final String message) {
//            if (value) {
//                throw Code.VALIDATED.exception(message);
//            }
//        }
//
//        /**
//         * 校验Collection集合不能为空且长度不能为0；否则抛出 Code.VALIDATED 异常
//         *
//         * @param values  Collection 集合
//         * @param message String 异常消息内容
//         */
//        public static void notEmpty(final Collection<?> values, final String message) {
//            if (Objects.isNull(values) || values.size() == 0) {
//                throw Code.VALIDATED.exception(message);
//            }
//        }
//
//        /**
//         * 校验Map集合不能为空且长度不能为0；否则抛出 Code.VALIDATED 异常
//         *
//         * @param values  Map 集合
//         * @param message String 异常消息内容
//         */
//        public static void notEmpty(final Map<?, ?> values, final String message) {
//            if (Objects.isNull(values) || values.size() == 0) {
//                throw Code.VALIDATED.exception(message);
//            }
//        }
//
//        /**
//         * 校验数组不能为空且长度不能为0；否则抛出 Code.VALIDATED 异常
//         *
//         * @param values  T[] 数组
//         * @param message String 异常消息内容
//         */
//        public static <T> void notEmpty(final T[] values, final String message) {
//            if (Objects.isNull(values) || values.length == 0) {
//                throw Code.VALIDATED.exception(message);
//            }
//        }
//
//        /**
//         * 校验对象不能为空；否则抛出 Code.VALIDATED 异常
//         *
//         * @param value   Object 判断值
//         * @param message String 异常消息内容
//         */
//        public static void notEmpty(final Object value, final String message) {
//            if (Objects.isNull(value)) {
//                throw Code.VALIDATED.exception(message);
//            }
//        }
//
//        /**
//         * 校验字符串不能为空；否则抛出 Code.VALIDATED 异常
//         *
//         * @param value   String 判断值
//         * @param message String 异常消息内容
//         */
//        public static void notEmpty(final String value, final String message) {
//            if (Objects.isNull(value) || value.trim().length() == 0) {
//                throw Code.VALIDATED.exception(message);
//            }
//        }
//
//        /**
//         * 校验数字必须大于0；否则抛出 Code.VALIDATED 异常
//         *
//         * @param value   Number 判断值
//         * @param message String 异常消息内容
//         */
//        public static void gtZore(final Number value, final String message) {
//            if (Objects.isNull(value) || value.doubleValue() <= 0) {
//                throw Code.VALIDATED.exception(message);
//            }
//        }
//    }

    public static void main(String[] args) {
        Result result = Param.of("{\"code\":\"TIMEOUT\"}").parseObject(Result.class);
        log.info("{}", result);
        log.info("{}", Param.of("{\"code\":\"FAILURE\"}").parseObject(new TypeReference<Result<Object>>() {
        }));

        log.info(JSON.toJSONString(Param.of("[\"123\",\"456\"]").parseArray(String.class)));
        log.info(JSON.toJSONString(Param.of("[\"789\",\"456\"]").parseObject(List.class)));

        log.info("{}", Param.of(Item.builder().label("选项").value("10").build()));
        log.info("{}", Param.of("[\"d:\\\\project\"]"));

//        {
//            try {
//                Asserts.isTrue(1 == 1, "断言：结果为 true > 不抛异常");
//                Asserts.isTrue(1 != 1, "断言：结果为 false > 抛异常");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                Asserts.isFalse(1 != 1, "断言：结果为 false > 不抛异常");
//                Asserts.isFalse(1 == 1, "断言：结果为 true > 抛异常");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                Asserts.gtZore(1, "断言：结果 必须大于0 > 不抛异常");
//                Asserts.gtZore(0, "断言：结果 必须大于0 > 抛异常");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                Asserts.notEmpty("aaa", "断言：结果 字段是否为空 > 不抛异常");
//                Asserts.notEmpty("", "断言：结果 字段是否为空 > 抛异常");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                Asserts.notEmpty(new HashSet<String>() {{
//                    add("Set");
//                }}, "断言：结果 Set 不能为空 > 不抛异常");
//                Asserts.notEmpty(new HashSet<>(), "断言：结果 Set 不能为空 > 抛异常");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                Asserts.notEmpty(Arrays.asList("a", "b"), "断言：结果 ArrayList 不能为空 > 不抛异常");
//                Asserts.notEmpty(new ArrayList<>(), "断言：结果 ArrayList 不能为空 > 抛异常");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }
}
