package <%=pkg%>.code.<%=javaname%>.web;

import com.alibaba.fastjson.JSON;
import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>;
import <%=pkg%>.business.user.entity.TabUser;
import <%=pkg%>.code.<%=javaname%>.service.<%=JavaName%>Service;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import com.support.mvc.web.IController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * 请求操作响应：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 */
@Api(tags = "<%=comment%>")
//@ApiSort(1) // 控制接口排序
@RequestMapping("/1/<%=java_name%>")
@Controller
@Slf4j
@RequiredArgsConstructor
public class <%=JavaName%>Controller implements IController<<%=id%>, <%=TabName%>> {

    private final <%=JavaName%>Service service;

    @PostMapping
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_save')")
    @ApiOperation(value = "1.新增<%=comment%>", tags = {"<%=date%>"})
    @ApiImplicitParam(name = "body", dataType = "<%=TabName%>", dataTypeClass = <%=TabName%>.class, required = true)
    @ApiOperationSupport(
            order = 1,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
            })
    @ResponseBody
    @Override
    public Result<<%=TabName%>> save(final String body) {
        return new Result<<%=TabName%>>().execute(result ->
                result.setSuccess(service.save(JSON.parseObject(body, <%=TabName%>.class), user.getId()))
        );
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_update')")
    @ApiOperation(value = "2.修改<%=comment%>", tags = {"<%=date%>"})
    @ApiImplicitParam(name = "body", dataType = "<%=TabName%>", dataTypeClass = <%=TabName%>.class, required = true)
    @ApiOperationSupport(
            order = 2,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
            })
    @ResponseBody
    @Override
    public Result<Void> update(final Long id, final String body) {
        return new Result<Void>().call(() -> service.update(id, user.getId(), JSON.parseObject(body, <%=TabName%>.class)));
    }

/*
    // 优先使用 deleteByUid 方法，可以阻止平行越权。 只有在实体没有 uid 的情况才能将该方法开放给前端
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "3.物理删除<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(order = 3) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<Void> deleteById(final Long id) {
        return new Result<Void>().call(() -> service.deleteById(id, user.getId()));
    }
*/
/*
    @DeleteMapping("/{id}/{uid}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "3.物理删除<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(order = 3) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<Void> deleteByUid(final Long id, final String uid) {
        return new Result<Void>().call(() -> service.deleteByUid(id, uid, user.getId()));
    }
*/
/*
    // 优先使用 markDeleteByUid 方法，可以阻止平行越权。 只有在实体没有 uid 的情况才能将该方法开放给前端
    @PatchMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "3.逻辑删除<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(order = 3) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<Void> markDeleteById(final Long id) {
        return new Result<Void>().call(() -> service.markDeleteById(id, user.getId()));
    }
*/

    @PatchMapping("/{id}/{uid}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "3.逻辑删除<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(order = 3) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<Void> markDeleteByUid(final Long id, final String uid) {
        return new Result<Void>().call(() -> service.markDeleteByUid(id, uid, user.getId()));
    }

/*
    @PatchMapping
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "4.批量逻辑删除<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(order = 4) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<Void> markDeleteByIds(final Set<Long> body) {
        return new Result<Void>().call(() -> service.markDelete(body, user.getId()));
    }
*/

    @PatchMapping
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "4.批量逻辑删除<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(order = 4) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<Void> markDelete(final List<MarkDelete> body) {
        return new Result<Void>().call(() -> service.markDelete(body, user.getId()));
    }

/*
    // 优先使用 findByUid 方法，可以阻止平行越权。 只有在实体没有 uid 的情况才能将该方法开放给前端
    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_find')")
    @ApiOperation(value = "5.按 id 查询<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(order = 5) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<?> findById(final Long id) {
        return new Result<<%=TabName%>>().execute(result -> result.setSuccess(service.findById(id).orElse(null)));
    }
*/

    @GetMapping("/{id}/{uid}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_find')")
    @ApiOperation(value = "5.按 id 和 uid 查询<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(order = 5) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<TabRole> findByUid(final Long id, final String uid) {
        return new Result<TabRole>().execute(result -> result.setSuccess(service.findByUid(id, uid).orElse(null)));
    }

    @GetMapping("/page/{number}/{size}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_page')")
    @ApiOperation(value = "6.分页查询<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(
            order = 6,
            ignoreParameters = {"insertTime", "updateTime"}
    )
    @ResponseBody
    @Override
    public Result<<%=TabName%>> page(final int number, final int size, final <%=TabName%> condition) {
        return new Result<<%=TabName%>>().execute(result -> result.setSuccess(service.findPage(
                Optional.ofNullable(condition).orElseGet(<%=TabName%>::new),
                Pager.builder().number(number).size(size).build()
        )));
    }
/*
    // 非必要情况下不要开放列表查询方法，因为没有分页控制，容易内存溢出。大批量查询数据应该使用分页查询
    @GetMapping
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_search')")
    @ApiOperation(value = "7.分页查询<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(
            order = 7,
            ignoreParameters = {"insertTime", "updateTime"}
    )
    @ResponseBody
    @Override
    public Result<<%=TabName%>> search(final <%=TabName%> condition) {
        return new Result<<%=TabName%>>().execute(result -> result.setSuccess(service.findList(
                Optional.ofNullable(condition).orElseGet(<%=TabName%>::new),
        )));
    }
*/
}
