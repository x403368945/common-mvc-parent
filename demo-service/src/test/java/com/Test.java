package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.HashBasedTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by 谢长春 on 2017/10/14.
 */
@Slf4j
public class Test {
    public static void main(String[] args) {

        JSONObject obj = new JSONObject();
        obj.put("date", "2017-12-12");
        System.out.println(obj.getDate("date"));

        Stream.of(
                User.builder().id(1).phone("18700000001").name("JX").value(System.currentTimeMillis()).build(),
                User.builder().id(2).phone("18700000002").name("Jack").value(System.currentTimeMillis()).build(),
                User.builder().id(3).phone("18700000003").name("Jhon").value(System.currentTimeMillis()).build(),
                User.builder().id(4).phone("18700000004").name("Jessie").value(System.currentTimeMillis()).build(),
                User.builder().id(5).phone("18700000005").name("Joe").value(System.currentTimeMillis()).build()
        ).collect(
                HashBasedTable::create,
                (t, u) -> {
                    System.out.println(t.getClass());
                    System.out.println(u.getClass());
                    System.out.println(JSON.toJSONString(Arrays.asList(t, u)));
                },
                (t, u) -> {
                    System.out.println(JSON.toJSONString(Arrays.asList(t, u)));
                }
        );

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    static class User {
        private int id;
        private String phone;
        private String name;
        private long value;

    }

}
