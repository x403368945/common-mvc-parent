package com.ccx.demo.business.example.web;

import com.ccx.demo.business.example.entity.DemoMongo;
import com.ccx.demo.business.example.entity.DemoMongo.OrderBy;
import com.ccx.demo.business.example.service.DemoMongoService;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.config.init.AppConfig.URL;
import com.ccx.demo.enums.Radio;
import com.ccx.demo.business.user.web.IAuthController;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Param;
import com.support.mvc.entity.base.Result;
import com.support.mvc.entity.base.Sorts;
import com.utils.util.Dates;
import com.utils.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

import static com.support.mvc.entity.base.Sorts.Direction.DESC;

/**
 * 请求操作响应：案例
 *
 *
 * @author 谢长春 2018-10-5
 */
@Controller
@RequestMapping("/demo-mongo/{version}")
@Slf4j
public class DemoMongoController implements IAuthController<String> {

    @Autowired
    private DemoMongoService service;

    @PostMapping
    @ResponseBody
    @Override
    public Result<?> save(@AuthenticationPrincipal final TabUser user,
                          @PathVariable final int version,
                          // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
                          @RequestBody(required = false) final Param param) {
        return new Result<DemoMongo>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(DemoMongo.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "保存数据",
                                "1.当前版本变更说明"
                        ))
                        .build()
                        .demo(v -> v.setDemo(
                                URL.SERVER.append(v.formatUrl()), // 当前接口参考案例请求地址；
                                DemoMongo.builder() // 当前接口参考案例请求参数，一般demo中存放必填字段或者所有字段
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.save(
                                Param.of(param).required().parseObject(DemoMongo.class),
                                user.getId()
                        ))
                );
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> update(@AuthenticationPrincipal final TabUser user,
                            @PathVariable final int version,
                            @PathVariable final String id,
                            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
                            @RequestBody(required = false) final Param param) {
        return new Result<>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(DemoMongo.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "更新数据",
                                "1.当前版本变更说明"
                        ))
                        .build()
                        .demo(v -> v.setDemo(
                                URL.SERVER.append(v.formatUrl(Util.uuid32())), // 当前接口参考案例请求地址；
                                DemoMongo.builder() // 当前接口参考案例请求参数，一般demo中存放必填字段或者所有字段
                                        .name("JX")
                                        .updateTime(Dates.now().timestamp())
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                       .call(() -> service.update(
                                id,
                                user.getId(),
                                Param.of(param).required().parseObject(DemoMongo.class)
                        ))
                );
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> deleteById(@AuthenticationPrincipal final TabUser user,
                                @PathVariable final int version,
                                @PathVariable final String id) {
        return new Result<DemoMongo>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(DemoMongo.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "物理删除数据",
                                "1.当前版本变更说明"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(Util.uuid32())))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.deleteById(id, user.getId()))
                );
    }

    @PatchMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> markDeleteById(@AuthenticationPrincipal final TabUser user,
                                    @PathVariable final int version,
                                    @PathVariable final String id) {
        return new Result<>(1)
                .version(this.getClass(), builder -> builder
                        .props(DemoMongo.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "逻辑删除数据",
                                "1.当前版本变更说明"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(Util.uuid32())))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                       .call(() -> service.markDeleteById(id, user.getId()))
                );
    }

    @PatchMapping
    @ResponseBody
    @Override
    public Result<?> markDelete(@AuthenticationPrincipal final TabUser user,
                                @PathVariable final int version,
                                @RequestBody(required = false) final Param param) {
        return new Result<>(1)
                .version(this.getClass(), builder -> builder
                                .props(DemoMongo.Props.list()) // 当前返回对象属性说明
                                .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                        "逻辑删除数据",
                                        "1.当前版本变更说明"
                                ))
                                .build()
                                .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()), // 当前接口参考案例请求地址；
                                        // 方案1：按 ID 逻辑删除
                                        Arrays.asList(Util.uuid32(), Util.uuid32())
                                        // 方案2：按 ID 和 UUID 逻辑删除
//                                Arrays.asList(
//                                        DemoMongo.builder().id(Util.uuid()).build(),
//                                        DemoMongo.builder().id(Util.uuid()).build()
//                                )
                                ))
                )
                .execute(result -> result
                                .versionAssert(version, false) // 弱校验版本号
                               .call(() -> service.markDeleteByIds(Param.of(param).required().hasArray().parseArray(String.class), user.getId())) // 方案1：按 ID 逻辑删除
//                       .call(() -> service.markDelete(Param.of(param).required().hasArray().parseArray(DemoMongo.class), user.getId())) // 方案2：按 ID 和 UUID 逻辑删除
                );
    }

    // 该方法与 findByIdTimestamp() 2选1 即可
/*
    @GetMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> findById(@AuthenticationPrincipal final TabUser user,
                              @PathVariable final int version,
                              @PathVariable final String id) {
        return new Result<TabDemoJpaMongo>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(TabDemoJpaMongo.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "按ID查询详细信息",
                                "1.当前版本变更说明"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100)))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findById(id).orElse(null))
                );
    }
*/


    @GetMapping("/{id}/{timestamp}")
    @ResponseBody
    @Override
    public Result<?> findByIdTimestamp(@AuthenticationPrincipal final TabUser user,
                                       @PathVariable final int version,
                                       @PathVariable final String id,
                                       @PathVariable final long timestamp) {
        return new Result<DemoMongo>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(DemoMongo.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "按ID查询 + 最后一次更新时间戳查询数据",
                                "1.当前版本变更说明"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(Util.uuid32(), Dates.now().getTimeMillis())))) // 当前接口参考案例请求地址；
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
            @AuthenticationPrincipal final TabUser user,
            @PathVariable final int version,
            @RequestParam(required = false, defaultValue = "{}") final String json) {
        return new Result<DemoMongo>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(DemoMongo.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "查询多条数据，不分页，url带参必须使用 encodeURI 格式化【?json=encodeURI(JSON.stringify({}))】",
                                "1.当前版本变更说明"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()), // 当前接口参考案例请求地址；
                                DemoMongo.builder() // 当前接口参考案例请求参数，demo中设置支持查询的字段
                                        .deleted(Radio.NO)
                                        .sorts(Collections.singletonList(Sorts.Order.builder().name(OrderBy.insertTime.name()).direction(DESC).build()))
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findList(
                                Param.of(json).parseObject(DemoMongo.class)
                        ))
                );
    }

    @GetMapping("/page/{number}/{size}")
    @ResponseBody
    @Override
    public Result<?> page(
            @AuthenticationPrincipal final TabUser user,
            @PathVariable final int version,
            @PathVariable final int number,
            @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final String json
    ) {
        return new Result<DemoMongo>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(DemoMongo.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "分页查询数据，url带参必须使用 encodeURI 格式化【?json=encodeURI(JSON.stringify({}))】",
                                "1.当前版本变更说明"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(1, 20)), // 当前接口参考案例请求地址；
                                DemoMongo.builder() // 当前接口参考案例请求参数，demo中设置支持查询的字段
                                        .deleted(Radio.NO)
                                        .sorts(Collections.singletonList(Sorts.Order.builder().name(OrderBy.insertTime.name()).direction(DESC).build()))
                                        .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findPage(
                                Param.of(json).parseObject(DemoMongo.class),
                                Pager.builder().number(number).size(size).build()
                        ))
                );
    }

}
