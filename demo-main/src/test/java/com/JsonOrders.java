package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mvc.demo.business.user.entity.TabUser;
import com.mvc.demo.config.init.AppConfig;
import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import com.utils.util.Util;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonOrders {

    public static void main(String[] args) {
//        JUnit.set();
        AppConfig.setDebug(true);

//		printFields(Result.class, "{\"code\":\"SUCCESS\",\"data\":[],\"message\":\"成功\",\"exception\":\"成功\",\"pageCount\":15,\"rowCount\":20,\"totalCount\":295}", new String[]{});
//        Result();
        {
            TabUser();
        }
    }

    /**
     * 忽略字段
     */
    enum Ignores {
        deleted,
        createTime,
        createUserId,
        createUserName,
        modifyTime,
        modifyUserId,
        modifyUserName;

        public static String[] toArrays() {
            return new String[]{deleted.name(), createTime.name(), createUserId.name(), createUserName.name(), modifyTime.name(), modifyUserId.name(), modifyUserName.name()};
        }

        public static String[] toArrays(Ignores... igs) {
            String[] values = new String[igs.length];
            for (int i = 0; i < igs.length; i++) {
                values[i] = igs[i].toString();
            }
            return values;
        }

        public static String[] toArrays(String... appends) {
            return ArrayUtils.addAll(appends, toArrays());
        }

        public static String[] toArrays(String[] appends, Ignores... igs) {
            return ArrayUtils.addAll(toArrays(igs), appends);
        }
    }

    private static String[] printFields(Class<?> clazz, String jsonText, String... ignores) {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if ("serialVersionUID".equals(field.getName())
                    || field.getName().startsWith("$")
                    || Util.in(field.getName(), Arrays.asList(ignores))) {
                continue;
            }
            fieldNames.add(field.getName());
        }
        JSONObject obj = JSON.parseObject(jsonText);
        for (String key : fieldNames) {
            obj.remove(key);
        }
        for (String key : ignores) {
            obj.remove(key);
        }
        if (obj.containsKey("createUserName")) {
            if (fieldNames.indexOf("createUserId") > 0) {
                fieldNames.add(fieldNames.indexOf("createUserId") + 1, "createUserName");
            } else if (fieldNames.indexOf("createTime") > 0) {
                fieldNames.add(fieldNames.indexOf("createTime") + 1, "createUserName");
            } else fieldNames.add("createUserName");
            obj.remove("createUserName");
        }
        if (obj.containsKey("modifyUserName")) {
            if (fieldNames.lastIndexOf("modifyUserId") > 0) {
                fieldNames.add(fieldNames.indexOf("modifyUserId") + 1, "modifyUserName");
            } else if (fieldNames.indexOf("modifyTime") > 0) {
                fieldNames.add(fieldNames.indexOf("modifyTime") + 1, "modifyUserName");
            } else fieldNames.add("modifyUserName");
            obj.remove("modifyUserName");
        }
        obj.keySet().forEach(key -> fieldNames.add(key));
        System.out.println("属性序列化排序:\n@JSONType(orders = {\"" + String.join("\",\"", fieldNames) + "\"})");
        System.out.println("扩展属性:\n\"" + String.join("\",\"", obj.keySet()) + "\"");
        return fieldNames.toArray(new String[fieldNames.size()]);
    }

    private static String toJsonString(Object obj) {
        return JSON.toJSONString(obj,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty
        );
    }

    private static void Result() {
        System.out.println("\n**********************" + Result.class + "**********************");
        String[] igs = new String[]{};
        System.out.println("忽略字段：" + JSON.toJSONString(igs));

        Result obj = new Result(Code.SUCCESS);
        String jsonText = toJsonString(obj);
        printFields(Result.class, jsonText, igs);
        System.out.println(jsonText);
        System.out.println(toJsonString(new Result<Result>().setSuccess(obj)));
    }

    private static void TabUser() {
        System.out.println("\n**********************" + TabUser.class + "**********************");
        String[] igs = Ignores.toArrays("password");
        System.out.println("忽略字段：" + JSON.toJSONString(igs));

        TabUser obj = new TabUser();
        String jsonText = toJsonString(obj);
        printFields(TabUser.class, jsonText, igs);
        System.out.println(jsonText);
        System.out.println(toJsonString(new Result<TabUser>().setSuccess(obj)));
    }

}