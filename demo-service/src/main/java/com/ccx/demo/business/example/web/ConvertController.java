package com.ccx.demo.business.example.web;
//
//import com.alibaba.fastjson.JSON;
//import com.ccx.demo.business.example.entity.TabConvert;
//import com.ccx.demo.business.example.service.ConvertService;
//import com.ccx.demo.business.user.entity.TabUser;
//import com.ccx.demo.business.user.web.IAuthController;
//import com.support.mvc.entity.base.Pager;
//import com.support.mvc.entity.base.Result;
//import io.swagger.annotations.ApiParam;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
///**
// * 请求操作响应：测试自定义 Convert 表
// *
// * @author 谢长春 on 2019-08-21
// */
//@Controller
//@RequestMapping("/convert/{version}")
//@Slf4j
//@RequiredArgsConstructor
//public class ConvertController implements IAuthController<Long> {
//
//    private final ConvertService service;
//
//    @PostMapping
//    @ResponseBody
//    @Override
//    public Result<?> save(@AuthenticationPrincipal final TabUser user,
//                          @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
//                          // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
//                          @RequestBody(required = false) final String body) {
//        return new Result<TabConvert>(1) // 指定接口最新版本号
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .setSuccess(service.save(
//                                JSON.parseObject(body, TabConvert.class),
//                                user.getId()
//                        ))
//                );
//    }
//
//    @PutMapping("/{id}")
//    @ResponseBody
//    @Override
//    public Result<?> update(@AuthenticationPrincipal final TabUser user,
//                            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
//
//@ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
//                            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
//                            @RequestBody(required = false) final String body) {
//        return new Result<>(1) // 指定接口最新版本号
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .call(() -> service.update(
//                                id,
//                                user.getId(),
//                                JSON.parseObject(body, TabConvert.class)
//                        ))
//                );
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseBody
//    @Override
//    public Result<?> deleteById(@AuthenticationPrincipal final TabUser user,
//                                @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
//                                @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id) {
//        return new Result<TabConvert>(1) // 指定接口最新版本号
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .setSuccess(service.deleteById(id, user.getId()))
//                );
//    }
//
//    @DeleteMapping("/{id}/{uid}")
//    @ResponseBody
//    @Override
//    public Result<?> deleteByUid(@AuthenticationPrincipal final TabUser user,
//                                 @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
//
//@ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
//                                 @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
//        return new Result<TabConvert>(1) // 指定接口最新版本号
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .setSuccess(service.deleteByUid(id, uid, user.getId()))
//                );
//    }
//
//    @PatchMapping("/{id}")
//    @ResponseBody
//    @Override
//    public Result<?> markDeleteById(@AuthenticationPrincipal final TabUser user,
//                                    @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
//                                    @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id) {
//        return new Result<>(1) // 指定接口最新版本号
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .call(() -> service.markDeleteById(id, user.getId()))
//                );
//    }
//
//    @PatchMapping("/{id}/{uid}")
//    @ResponseBody
//    @Override
//    public Result<?> markDeleteByUid(@AuthenticationPrincipal final TabUser user,
//                                     @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
//
//@ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
//                                     @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
//        return new Result<>(1) // 指定接口最新版本号
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .call(() -> service.markDeleteByUid(id, uid, user.getId()))
//                );
//    }
//
//    @PatchMapping
//    @ResponseBody
//    @Override
//    public Result<?> markDelete(@AuthenticationPrincipal final TabUser user,
//                                @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
//                                @RequestBody(required = false) final String body) {
//        return new Result<>(1) // 指定接口最新版本号
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        // .call(()->service.markDeleteByIds(JSON.parseArray(body, Long.class), user.getId())) // 方案1：按 ID 逻辑删除
//                        .call(() -> service.markDelete(JSON.parseArray(body, TabConvert.class), user.getId())) // 方案2：按 ID 和 UUID 逻辑删除
//                );
//    }
//
///*
//    @GetMapping("/{id}/{uid}")
//    @ResponseBody
//    @Override
//    public Result<?> findByUid(@AuthenticationPrincipal final TabUser user,
//                               @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
//
//@ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
//                               @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
//        return new Result<TabConvert>(1) // 指定接口最新版本号
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .setSuccess(service.findByUid(id, uid).orElse(null))
//                );
//    }
//
//    @GetMapping
//    @ResponseBody
//    @Override
//    public Result<?> search(
//            @AuthenticationPrincipal final TabUser user,
//            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
//            @RequestParam(required = false, defaultValue = "{}") final String json) {
//        return new Result<TabConvert>(1) // 指定接口最新版本号
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .setSuccess(service.findList(
//                                JSON.parseObject(json, TabConvert.class)
//                        ))
//                );
//    }
//
//    @GetMapping("/page/{number}/{size}")
//    @ResponseBody
//    @Override
//    public Result<?> page(
//            @AuthenticationPrincipal final TabUser user,
//            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
//            @ApiParam(required = true, value = "页码", example = "1") @PathVariable final int number,
//            @ApiParam(required = true, value = "每页条数", example = "1") @PathVariable final int size,
//            @RequestParam(required = false, defaultValue = "{}") final String json
//    ) {
//        return new Result<TabConvert>(1) // 指定接口最新版本号
//                .execute(result -> result
//                        .versionAssert(version, false) // 弱校验版本号
//                        .setSuccess(service.findPage(
//                                JSON.parseObject(json, TabConvert.class),
//                                Pager.builder().number(number).size(size).build()
//                        ))
//                );
//    }
//
//}
