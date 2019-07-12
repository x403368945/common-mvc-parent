package com.ccx.business.{javaname}.web;

import com.ccx.business.{javaname}.entity.{TabName};
import com.ccx.business.{javaname}.entity.{TabName}.OrderBy;
import com.ccx.business.{javaname}.service.{JavaName}Service;
import {pkg}.config.init.AppConfig.URL;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Param;
import com.support.mvc.entity.base.Result;
import com.support.mvc.entity.base.Sorts;
import com.support.mvc.web.IController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

import static com.support.mvc.entity.base.Sorts.Direction.DESC;

/**
 * 请求操作响应：{comment}
 *
 * @author 谢长春 on {date}
 */
@Controller
@RequestMapping("/{java_name}/{version}")
@Slf4j
public class {JavaName}Controller implements IController<{ID}> {

    @Autowired
    private {JavaName}Service service;

    @GetMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> findById(@PathVariable final int version,
                              @PathVariable final Long id) {
        return new Result<{TabName}>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props({TabName}.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "按ID查询详细信息，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}",
                                "1. {comment}"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100)))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findById(id).orElse(null))
                );
    }

    @GetMapping
    @ResponseBody
    @Override
    public Result<?> search(
            @PathVariable final int version,
            @RequestParam(required = false, defaultValue = "{}") final String json) {
        return new Result<{TabName}>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props({TabName}.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "查询多条数据，不分页，url带参必须使用 encodeURI 格式化【?json=encodeURI(JSON.stringify({}))】，url带参说明:/{version【response.version.id】}",
                            "1. {comment}"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()), // 当前接口参考案例请求地址；
                                {TabName}.builder() // 当前接口参考案例请求参数，demo中设置支持查询的字段
                                    .sorts(Collections.singletonList(Sorts.Order.builder().name(OrderBy.id.name()).direction(DESC).build()))
                                    .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findList(
                                Param.of(json).parseObject({TabName}.class)
                        ))
                );
    }

    @GetMapping("/page/{number}/{size}")
    @ResponseBody
    @Override
    public Result<?> page(
            @PathVariable final int version,
            @PathVariable final int number,
            @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final String json
    ) {
        return new Result<{TabName}>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props({TabName}.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "分页查询数据，url带参必须使用 encodeURI 格式化【?json=encodeURI(JSON.stringify({}))】，url带参说明:/{version【response.version.id】}/page/{number【当前页码】}/{size【每页显示条数】}",
                            "1. {comment}"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(1, 20)), // 当前接口参考案例请求地址；
                                {TabName}.builder() // 当前接口参考案例请求参数，demo中设置支持查询的字段
                                        .sorts(Collections.singletonList(Sorts.Order.builder().name(OrderBy.id.name()).direction(DESC).build()))
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findPage(
                                Param.of(json).parseObject({TabName}.class),
                                Pager.builder().number(number).size(size).build()
                        ))
                );
    }

}
