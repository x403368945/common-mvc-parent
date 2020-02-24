package com.ccx.demo.business.example.web;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.example.entity.TabDemoList;
import com.ccx.demo.business.example.service.DemoListService;
import com.ccx.demo.business.example.vo.TabDemoListVO;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.web.IAuthController;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 请求操作响应：案例
 *
 * @author 谢长春 2018-10-5
 */
@Controller
@RequestMapping("/demo-list/{version}")
@Slf4j
public class DemoListController implements IAuthController<Long> {

    @Autowired
    private DemoListService service;

    @PostMapping
    @ResponseBody
    @Override
    public Result<?> save(@AuthenticationPrincipal final TabUser user,
                          @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                          // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
                          @RequestBody(required = false) final String body) {
        return new Result<TabDemoList>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.save(
                                JSON.parseObject(body, TabDemoList.class),
                                user.getId()
                        ))
                );
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> update(@AuthenticationPrincipal final TabUser user,
                            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                            @PathVariable final Long id,
                            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
                            @RequestBody(required = false) final String body) {
        return new Result<>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.update(
                                id,
                                user.getId(),
                                JSON.parseObject(body, TabDemoList.class)
                        ))
                );
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> deleteById(@AuthenticationPrincipal final TabUser user,
                                @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                                @PathVariable final Long id) {
        return new Result<TabDemoList>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.deleteById(id, user.getId()))
                );
    }

    @DeleteMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> deleteByUid(@AuthenticationPrincipal final TabUser user,
                                 @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                                 @PathVariable final Long id,
                                 @PathVariable final String uid) {
        return new Result<TabDemoList>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.deleteByUid(id, uid, user.getId()))
                );
    }

    @PatchMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> markDeleteById(@AuthenticationPrincipal final TabUser user,
                                    @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                                    @PathVariable final Long id) {
        return new Result<>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.markDeleteById(id, user.getId()))
                );
    }

    @PatchMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> markDeleteByUid(@AuthenticationPrincipal final TabUser user,
                                     @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                                     @PathVariable final Long id,
                                     @PathVariable final String uid) {
        return new Result<>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.markDeleteByUid(id, uid, user.getId()))
                );
    }

    @PatchMapping
    @ResponseBody
    @Override
    public Result<?> markDelete(@AuthenticationPrincipal final TabUser user,
                                @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                                @RequestBody(required = false) final String body) {
        return new Result<>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        //.call(()->service.markDeleteByIds(JSON.parseArray(body, Long.class), user.getId())) // 方案1：按 ID 逻辑删除
                        .call(() -> service.markDelete(JSON.parseArray(body, TabDemoList.class), user.getId())) // 方案2：按 ID 和 UUID 逻辑删除
                );
    }

    // 该方法与 findByIdTimestamp() 2选1 即可
/*
    @GetMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> findById(@AuthenticationPrincipal final TabUser user,
                              @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                              @PathVariable final Long id) {
        return new Result<TabDemoList>(1) // 指定接口最新版本号
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
                                       @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                                       @PathVariable final Long id,
                                       @PathVariable final long timestamp) {
        return new Result<TabDemoList>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findById(id).orElse(null))
                );
    }

    // 该方法与 findByUidTimestamp() 2选1 即可
/*
    @GetMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> findByUid(@AuthenticationPrincipal final TabUser user,
                               @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                               @PathVariable final Long id,
                               @PathVariable final String uid) {
        return new Result<TabDemoList>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findByUid(id, uid).orElse(null))
                );
    }
*/

    @GetMapping("/{id}/{uid}/{timestamp}")
    @ResponseBody
    @Override
    public Result<?> findByUidTimestamp(@AuthenticationPrincipal final TabUser user,
                                        @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
                                        @PathVariable final Long id,
                                        @PathVariable final String uid,
                                        @PathVariable final long timestamp) {
        return new Result<TabDemoList>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(
                                service.findByUid(id, uid).orElse(null)
                        )
                );
    }

    @GetMapping
    @ResponseBody
    @Override
    public Result<?> search(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
            @RequestParam(required = false, defaultValue = "{}") final String json) {
        return new Result<TabDemoList>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findList(
                                JSON.parseObject(json, TabDemoList.class)
                        ))
                );
    }

    @GetMapping("/page/{number}/{size}")
    @ResponseBody
    @Override
    public Result<?> page(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
            @PathVariable final int number,
            @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final String json
    ) {
        return new Result<TabDemoList>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findPage(
                                JSON.parseObject(json, TabDemoList.class),
                                Pager.builder().number(number).size(size).build()
                        ))
                );
    }


    @GetMapping("/vo")
    @ResponseBody
    public Result<?> searchVO(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
            @RequestParam(required = false, defaultValue = "{}") final String json) {
        return new Result<TabDemoListVO>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findListVO(
                                JSON.parseObject(json, TabDemoList.class)
                        ))
                );
    }

    @GetMapping("/page/vo/{number}/{size}")
    @ResponseBody
    public Result<?> pageVO(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
            @PathVariable final int number,
            @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final String json
    ) {
        return new Result<TabDemoListVO>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findPageVO(
                                JSON.parseObject(json, TabDemoList.class),
                                Pager.builder().number(number).size(size).build()
                        ))
                );
    }

    @GetMapping("/test")
    @ResponseBody
    public Result<?> test(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "版本号", example = "1") @PathVariable final int version,
            @RequestParam(required = false, defaultValue = "{}") final String json) {
        return new Result<TabDemoList>(1) // 指定接口最新版本号
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .call(() -> service.findListTest())
                );
    }
}
