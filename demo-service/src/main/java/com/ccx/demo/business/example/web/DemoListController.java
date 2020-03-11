package com.ccx.demo.business.example.web;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.example.entity.TabDemoList;
import com.ccx.demo.business.example.service.DemoListService;
import com.ccx.demo.business.example.vo.TabDemoListVO;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.business.user.web.IAuthController;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.Api;
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
import java.util.Optional;
import java.util.Set;

/**
 * 请求操作响应：测试案例表
 *
 * @author 谢长春 2018-10-5
 */
@Api(tags = "测试案例表")
@ApiSort(9) // 控制接口排序 <
@RequestMapping("/1/demo-list")
@Controller
@Slf4j
@RequiredArgsConstructor
public class DemoListController implements IAuthController<Long, TabDemoList> {

    private final DemoListService service;

    @PostMapping
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_save')") // <
    @ApiOperation(value = "1.新增测试案例表", tags = {"2020-03-11"})
    @ApiImplicitParam(name = "body", dataType = "TabDemoList", dataTypeClass = TabDemoList.class, required = true)
    @ApiOperationSupport(
            order = 1,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
            })
    @ResponseBody
    @Override
    public Result<TabDemoList> save(final TabUser user, final String body) {
        return new Result<TabDemoList>().execute(result -> result.setSuccess(
                service.save(JSON.parseObject(body, TabDemoList.class), user.getId())
        ));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_update')") // <
    @ApiOperation(value = "2.修改测试案例表", tags = {"2020-03-11"})
    @ApiImplicitParam(name = "body", dataType = "TabDemoList", dataTypeClass = TabDemoList.class, required = true)
    @ApiOperationSupport(
            order = 2,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
            })
    @ResponseBody
    @Override
    public Result<Void> update(final TabUser user, final Long id, final String body) {
        return new Result<Void>().call(() -> service.update(id, user.getId(), JSON.parseObject(body, TabDemoList.class)));
    }

    // 优先使用 deleteByUid 方法，可以阻止平行越权。 只有在实体没有 uid 的情况才能将该方法开放给前端<
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "3.物理删除测试案例表", tags = {"2020-03-11"})
    @ApiOperationSupport(order = 3) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<Void> deleteById(final TabUser user, final Long id) {
        return new Result<Void>().call(() -> service.deleteById(id, user.getId()));
    }

    @DeleteMapping("/{id}/{uid}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "4.物理删除测试案例表", tags = {"2020-03-11"})
    @ApiOperationSupport(order = 4) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<Void> deleteByUid(final TabUser user, final Long id, final String uid) {
        return new Result<Void>().call(() -> service.deleteByUid(id, uid, user.getId()));
    }

    @PatchMapping("/{id}/{uid}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "5.逻辑删除测试案例表", tags = {"2020-03-11"})
    @ApiOperationSupport(order = 5) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<Void> markDeleteByUid(final TabUser user, final Long id, final String uid) {
        return new Result<Void>().call(() -> service.markDeleteByUid(id, uid, user.getId()));
    }

    @PatchMapping
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "6.批量逻辑删除测试案例表", tags = {"2020-03-11"})
    @ApiOperationSupport(order = 6) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<Void> markDeleteByIds(final TabUser user, final Set<Long> body) {
        return new Result<Void>().call(() -> service.markDeleteByIds(body, user.getId()));
    }

    @PatchMapping
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "7.批量逻辑删除测试案例表", tags = {"2020-03-11"})
    @ApiOperationSupport(order = 7) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<Void> markDelete(final TabUser user, final List<MarkDelete> body) {
        return new Result<Void>().call(() -> service.markDelete(body, user.getId()));
    }

    // 优先使用 findByUid 方法，可以阻止平行越权。 只有在实体没有 uid 的情况才能将该方法开放给前端<
    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_find')")
    @ApiOperation(value = "8.按 id 查询测试案例表", tags = {"2020-03-11"})
    @ApiOperationSupport(order = 8) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<TabDemoList> findById(final TabUser user, final Long id) {
        return new Result<TabDemoList>().execute(result -> result.setSuccess(service.findById(id).orElse(null)));
    }

    @GetMapping("/{id}/{uid}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_find')")
    @ApiOperation(value = "9.按 id 和 uid 查询测试案例表", tags = {"2020-03-11"})
    @ApiOperationSupport(order = 9) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<TabDemoList> findByUid(final TabUser user, final Long id, final String uid) {
        return new Result<TabDemoList>().execute(result -> result.setSuccess(service.findByUid(id, uid).orElse(null)));
    }


    @GetMapping("/page/{number}/{size}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_page')") // <
    @ApiOperation(value = "10.分页查询测试案例表", tags = {"2020-03-11"})
    @ApiOperationSupport(
            order = 10,
            ignoreParameters = {"insertTime", "updateTime"}
    )
    @ResponseBody
    @Override
    public Result<TabDemoList> page(final TabUser user, final int number, final int size, final TabDemoList condition) {
        return new Result<TabDemoList>().execute(result -> result.setSuccess(service.findPage(
                Optional.ofNullable(condition).orElseGet(TabDemoList::new),
                Pager.builder().number(number).size(size).build()
        )));
    }

    // 非必要情况下不要开放列表查询方法，因为没有分页控制，容易内存溢出。大批量查询数据应该使用分页查询<
    @GetMapping
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_search')")
    @ApiOperation(value = "11.分页查询测试案例表", tags = {"2020-03-11"})
    @ApiOperationSupport(
            order = 11,
            ignoreParameters = {"insertTime", "updateTime"}
    )
    @ResponseBody
    @Override
    public Result<TabDemoList> search(final TabUser user, final TabDemoList condition) {
        return new Result<TabDemoList>().execute(result -> result.setSuccess(service.findList(
                Optional.ofNullable(condition).orElseGet(TabDemoList::new)
        )));
    }

    @GetMapping("/page/vo/{number}/{size}")
    @ApiOperation(value = "12.鉴权测试 GET 分页查询， 扩展 10 的查询条件和返回参数", tags = {"0.0.0"})
    @ApiOperationSupport(order = 12)
    @ResponseBody
    public Result<TabDemoListVO> pageVO(
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            @ApiParam(required = true, value = "页码", example = "1") @PathVariable final int number,
            @ApiParam(required = true, value = "每页条数", example = "1") @PathVariable final int size,
            final TabDemoListVO condition) {
        return new Result<TabDemoListVO>().execute(result -> result.setSuccess(service.findPageVO(
                condition,
                Pager.builder().number(number).size(size).build())
        ));
    }

    @GetMapping("/vo")
    @ApiOperation(value = "13.鉴权测试 GET 查询多条记录， 扩展 11 的查询条件和返回参数", tags = {"0.0.0"})
    @ApiOperationSupport(order = 13)
    @ResponseBody
    public Result<TabDemoListVO> searchVO(
            @ApiIgnore @AuthenticationPrincipal final TabUser user,
            final TabDemoListVO condition) {
        return new Result<TabDemoListVO>().execute(result -> result.setSuccess(service.findListVO(condition)));
    }

    @ApiIgnore
    @GetMapping("/test")
    @ResponseBody
    public Result<Void> test(@ApiIgnore @AuthenticationPrincipal final TabUser user) {
        return new Result<Void>().call(service::findListTest);
    }

}
