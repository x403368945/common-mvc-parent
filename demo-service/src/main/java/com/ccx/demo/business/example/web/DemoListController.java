package com.ccx.demo.business.example.web;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.example.entity.TabDemoList;
import com.ccx.demo.business.example.service.DemoListService;
import com.ccx.demo.business.example.vo.TabDemoListVO;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.web.IAuthController;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 请求操作响应：案例
 *
 * @author 谢长春 2018-10-5
 */
@RequestMapping("/1/demo-list")
@Controller
@Slf4j
@RequiredArgsConstructor
public class DemoListController implements IAuthController<Long, TabDemoList> {

    private final DemoListService service;

    @PostMapping
    @ApiOperation(value = "1.鉴权测试 POST 保存", tags = {"0.0.0"})
    @ApiImplicitParam(name = "body", dataType = "TabDemoList", dataTypeClass = TabDemoList.class, required = true)
    @ApiOperationSupport(order = 1, ignoreParameters = {"body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"})
    @ResponseBody
    @Override
    public Result<TabDemoList> save(
            @AuthenticationPrincipal final TabUser user,
            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
            @RequestBody(required = false) final String body) {
        return new Result<TabDemoList>().execute(result ->
                result.setSuccess(service.save(JSON.parseObject(body, TabDemoList.class), user.getId()))
        );
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "2.鉴权测试 PUT 全量更新", tags = {"0.0.0"})
    @ApiImplicitParam(name = "body", dataType = "TabDemoList", dataTypeClass = TabDemoList.class, required = true)
    @ApiOperationSupport(order = 2, ignoreParameters = {"body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.sorts"})
    @ResponseBody
    @Override
    public Result<Void> update(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
            @RequestBody(required = false) final String body) {
        return new Result<Void>().call(() -> service.update(id, user.getId(), JSON.parseObject(body, TabDemoList.class)));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "3.鉴权测试 DELETE 按 id 删除", tags = {"0.0.0"})
    @ApiOperationSupport(order = 3)
    @ResponseBody
    @Override
    public Result<Void> deleteById(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id) {
        return new Result<Void>().call(() -> service.deleteById(id, user.getId()));
    }

    @DeleteMapping("/{id}/{uid}")
    @ApiOperation(value = "4.鉴权测试 DELETE 按 id 和 uid 删除", tags = {"0.0.0"})
    @ApiOperationSupport(order = 4)
    @ResponseBody
    @Override
    public Result<Void> deleteByUid(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
            @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return new Result<Void>().call(() -> service.deleteByUid(id, uid, user.getId()));
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "5.鉴权测试 PATCH 按 id 逻辑删除", tags = {"0.0.0"})
    @ApiOperationSupport(order = 5)
    @ResponseBody
    @Override
    public Result<Void> markDeleteById(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id) {
        return new Result<Void>().call(() -> service.markDeleteById(id, user.getId()));
    }

    @PatchMapping("/{id}/{uid}")
    @ApiOperation(value = "6.鉴权测试 PATCH 按 id 和 uid 逻辑删除", tags = {"0.0.0"})
    @ApiOperationSupport(order = 6)
    @ResponseBody
    @Override
    public Result<Void> markDeleteByUid(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
            @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return new Result<Void>().call(() -> service.markDeleteByUid(id, uid, user.getId()));
    }

    @PatchMapping
    @ApiOperation(value = "7.鉴权测试 PATCH 按 id 和 uid 批量逻辑删除", tags = {"0.0.0"})
    @ApiOperationSupport(order = 7)
    @ResponseBody
    @Override
    public Result<Void> markDelete(
            @AuthenticationPrincipal final TabUser user,
            @RequestBody(required = false) final List<MarkDelete> body) {
        return new Result<Void>().call(() -> service.markDelete(body, user.getId())); // 按 ID 和 UUID 逻辑删除
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "8.鉴权测试 GET 按 id 查询单条记录", tags = {"0.0.0"})
    @ApiOperationSupport(order = 8)
    @ResponseBody
    @Override
    public Result<TabDemoList> findById(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id) {
        return new Result<TabDemoList>().execute(result -> result.setSuccess(service.findById(id).orElse(null)));
    }

    @GetMapping("/{id}/{uid}")
    @ApiOperation(value = "9.鉴权测试 GET 按 id 和 uid 查询单条记录", tags = {"0.0.0"})
    @ApiOperationSupport(order = 9)
    @ResponseBody
    @Override
    public Result<TabDemoList> findByUid(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
            @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return new Result<TabDemoList>().execute(result -> result.setSuccess(service.findByUid(id, uid).orElse(null)));
    }

    @GetMapping
    @ApiOperation(value = "10.鉴权测试 GET 查询多条记录", tags = {"0.0.0"})
    @ApiOperationSupport(order = 10)
    @ResponseBody
    @Override
    public Result<TabDemoList> search(
            @AuthenticationPrincipal final TabUser user,
            @RequestParam(required = false, defaultValue = "{}") final TabDemoList condition) {
        return new Result<TabDemoList>().execute(result -> result.setSuccess(service.findList(condition)));
    }

    @GetMapping("/page/{number}/{size}")
    @ApiOperation(value = "11.鉴权测试 GET 分页查询", tags = {"0.0.0"})
    @ApiOperationSupport(order = 11)
    @ResponseBody
    @Override
    public Result<TabDemoList> page(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "页码", example = "1") @PathVariable final int number,
            @ApiParam(required = true, value = "每页条数", example = "1") @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final TabDemoList condition) {
        return new Result<TabDemoList>().execute(result -> result.setSuccess(service.findPage(condition, Pager.builder().number(number).size(size).build())));
    }

    @GetMapping("/vo")
    @ApiOperation(value = "12.鉴权测试 GET 查询多条记录， 扩展 10 的查询条件和返回参数", tags = {"0.0.0"})
    @ApiOperationSupport(order = 12)
    @ResponseBody
    public Result<TabDemoListVO> searchVO(
            @AuthenticationPrincipal final TabUser user,
            @RequestParam(required = false, defaultValue = "{}") final TabDemoListVO condition) {
        return new Result<TabDemoListVO>().execute(result -> result.setSuccess(service.findListVO(condition)));
    }

    @GetMapping("/page/vo/{number}/{size}")
    @ApiOperation(value = "13.鉴权测试 GET 分页查询， 扩展 11 的查询条件和返回参数", tags = {"0.0.0"})
    @ApiOperationSupport(order = 13)
    @ResponseBody
    public Result<TabDemoListVO> pageVO(
            @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "页码", example = "1") @PathVariable final int number,
            @ApiParam(required = true, value = "每页条数", example = "1") @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final TabDemoListVO condition) {
        return new Result<TabDemoListVO>().execute(result -> result.setSuccess(service.findPageVO(
                condition,
                Pager.builder().number(number).size(size).build())
        ));
    }

    @ApiIgnore
    @GetMapping("/test")
    @ResponseBody
    public Result<Void> test(@AuthenticationPrincipal final TabUser user) {
        return new Result<Void>().call(service::findListTest);
    }
}
