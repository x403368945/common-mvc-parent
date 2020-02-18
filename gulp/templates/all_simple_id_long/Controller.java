package <%=pkg%>.code.<%=javaname%>.web;

import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>;
import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>.OrderBy;
import <%=pkg%>.code.<%=javaname%>.service.<%=JavaName%>Service;
import <%=pkg%>.config.init.AppConfig.URL;
import <%=pkg%>.enums.Bool;
import com.support.mvc.web.IController;
import com.support.mvc.entity.base.*;
import com.utils.util.Dates;
import com.utils.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

import static com.support.mvc.entity.base.Sorts.Direction.DESC;

/**
 * 请求操作响应：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 */
@Controller
@RequestMapping("/<%=java_name%>/{version}")
@Slf4j
public class <%=JavaName%>Controller implements IController<<%=id%>> {

    @Autowired
    private <%=JavaName%>Service service;

    @PostMapping
    @ResponseBody
    @Override
    public Result<?> save(@PathVariable final int version,
                          // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
                          @RequestBody(required = false) final Param param) {
        return new Result<<%=TabName%>>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "保存数据，url带参说明:/{version【response.version.id】}",
                                "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(
                                URL.SERVER.append(v.formatUrl()), // 当前接口参考案例请求地址；
                                <%=TabName%>.builder() // 当前接口参考案例请求参数，一般demo中存放必填字段或者所有字段
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.save(
                                Param.of(param).required().parseObject(<%=TabName%>.class)
                        ))
                );
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> update(@PathVariable final int version,
                            @PathVariable final <%=id%> id,
                            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
                            @RequestBody(required = false) final Param param) {
        return new Result<>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "更新数据，不带 uid 强校验，但可能会带当前操作人校验，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}",
                            "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(
                                URL.SERVER.append(v.formatUrl(100)), // 当前接口参考案例请求地址；
                                <%=TabName%>.builder() // 当前接口参考案例请求参数，一般demo中存放必填字段或者所有字段
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.update(
                                id,
                                Param.of(param).required().parseObject(<%=TabName%>.class)
                        ))
                );
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> deleteById(@PathVariable final int version,
                                @PathVariable final <%=id%> id) {
        return new Result<<%=TabName%>>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "物理删除数据，不带 uid 强校验，但可能会带当前操作人校验，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}",
                            "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100)))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.deleteById(id))
                );
    }


    @PatchMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> markDeleteById(@PathVariable final int version,
                                    @PathVariable final <%=id%> id) {
        return new Result<>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "逻辑删除数据，不带 uid 强校验，但可能会带当前操作人校验，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}",
                            "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100)))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.markDeleteById(id))
                );
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> findById(@PathVariable final int version,
                              @PathVariable final Long id) {
        return new Result<<%=TabName%>>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "按ID查询详细信息，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}",
                                "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100)))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findById(id).orElse(null))
                );
    }
//
//    @GetMapping
//    @ResponseBody
//    @Override
//    public Result<?> search(
//            @PathVariable final int version,
//            @RequestParam(required = false, defaultValue = "{}") final String json) {
//        return new Result<<%=TabName%>>(1) // 指定接口最新版本号
//                .version(this.getClass(), builder -> builder
//                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
//                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
//                            "查询多条数据，不分页，url带参必须使用 encodeURI 格式化【?json=encodeURI(JSON.stringify({}))】，url带参说明:/{version【response.version.id】}",
//                            "1. <%=comment%>"
//                        ))
//                        .build()
//                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()), // 当前接口参考案例请求地址；
//                                <%=TabName%>.builder() // 当前接口参考案例请求参数，demo中设置支持查询的字段
//                                    .deleted(Bool.NO)
//                                    .sorts(Collections.singletonList(Sorts.Order.builder().name(OrderBy.id.name()).direction(DESC).build()))
//                                    .build()
//                        ))
//                )
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .setSuccess(service.findList(
//                                Param.of(json).parseObject(<%=TabName%>.class)
//                        ))
//                );
//    }

    @GetMapping("/page/{number}/{size}")
    @ResponseBody
    @Override
    public Result<?> page(
            @PathVariable final int version,
            @PathVariable final int number,
            @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final String json
    ) {
        return new Result<<%=TabName%>>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "分页查询数据，url带参必须使用 encodeURI 格式化【?json=encodeURI(JSON.stringify({}))】，url带参说明:/{version【response.version.id】}/page/{number【当前页码】}/{size【每页显示条数】}",
                            "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(1, 20)), // 当前接口参考案例请求地址；
                                <%=TabName%>.builder() // 当前接口参考案例请求参数，demo中设置支持查询的字段
//                                        .deleted(Bool.NO)
                                        .sorts(Collections.singletonList(Sorts.Order.builder().name(OrderBy.id.name()).direction(DESC).build()))
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findPage(
                                Param.of(json).parseObject(<%=TabName%>.class),
                                Pager.builder().number(number).size(size).build()
                        ))
                );
    }

}
