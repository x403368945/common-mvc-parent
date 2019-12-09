package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.config.init.AppConfig;
import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import com.utils.util.Util;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class JsonOrders {

    public static void main(String[] args) {
//        JUnit.set();

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
        insertTime,
        insertUserId,
        insertUserName,
        updateTime,
        updateUserId,
        updateUserName;

        public static String[] toArrays() {
            return new String[]{deleted.name(), insertTime.name(), insertUserId.name(), insertUserName.name(), updateTime.name(), updateUserId.name(), updateUserName.name()};
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
        if (obj.containsKey("insertUserName")) {
            if (fieldNames.indexOf("insertUserId") > 0) {
                fieldNames.add(fieldNames.indexOf("insertUserId") + 1, "insertUserName");
            } else if (fieldNames.indexOf("insertTime") > 0) {
                fieldNames.add(fieldNames.indexOf("insertTime") + 1, "insertUserName");
            } else fieldNames.add("insertUserName");
            obj.remove("insertUserName");
        }
        if (obj.containsKey("updateUserName")) {
            if (fieldNames.lastIndexOf("updateUserId") > 0) {
                fieldNames.add(fieldNames.indexOf("updateUserId") + 1, "updateUserName");
            } else if (fieldNames.indexOf("updateTime") > 0) {
                fieldNames.add(fieldNames.indexOf("updateTime") + 1, "updateUserName");
            } else fieldNames.add("updateUserName");
            obj.remove("updateUserName");
        }
        fieldNames.addAll(obj.keySet());
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