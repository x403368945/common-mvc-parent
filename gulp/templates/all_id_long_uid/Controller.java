package <%=pkg%>.code.<%=javaname%>.web;

import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>;
import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>.OrderBy;
import <%=pkg%>.code.<%=javaname%>.service.<%=JavaName%>Service;
import <%=pkg%>.business.user.entity.TabUser;
import <%=pkg%>.business.user.web.IAuthController;
import <%=pkg%>.config.init.AppConfig.URL;
import <%=pkg%>.enums.Bool;
import com.support.mvc.entity.base.*;
import com.utils.util.Dates;
import com.utils.util.Util;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.ApiParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

/**
 * 请求操作响应：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 */
@Api(tags = "<%=comment%>")
//@ApiSort(3) // 控制接口排序
@RequestMapping("/<%=java_name%>/{version}")
@Controller
@Slf4j
@RequiredArgsConstructor
public class <%=JavaName%>Controller implements IAuthController<<%=id%>, <%=TabName%>> {

    private final <%=JavaName%>Service service;

    @PostMapping
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_save')")
    @ApiOperation(value = "1.新增<%=comment%>", tags = {"<%=date%>"})
    @ApiImplicitParam(name = "body", dataType = "<%=TabName%>", dataTypeClass = <%=TabName%>.class, required = true)
    @ApiOperationSupport(
            order = 1,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
            })
    @ResponseBody
    @Override
    public Result<<%=TabName%>> save(final TabUser user, final String body) {
        return new Result<<%=TabName%>>()
                .execute(result -> result.setSuccess(service.save(JSON.parseObject(body, <%=TabName%>.class), user.getId())));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_update')")
    @ApiOperation(value = "2.修改<%=comment%>", tags = {"<%=date%>"})
    @ApiImplicitParam(name = "body", dataType = "<%=TabName%>", dataTypeClass = <%=TabName%>.class, required = true)
    @ApiOperationSupport(
            order = 2,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
            })
    @ResponseBody
    @Override
    public Result<Void> update(final TabUser user, final <%=id%> id, final String body) {
        return new Result<Void>()
                .call(() -> service.update(id, user.getId(), JSON.parseObject(body, <%=TabName%>.class)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "3.逻辑删除角色", tags = {"1.0.0"})
    @ApiOperationSupport(order = 3)
    @ResponseBody
    @Override
    public Result<Void> deleteById(final TabUser user,final int version,final <%=id%> id) {
        return new Result<Void>()
                .call(() -> service.deleteById(id, user.getId()));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> deleteById(@AuthenticationPrincipal final TabUser user,

                                @PathVariable final <%=id%> id) {
        return new Result<<%=TabName%>>()
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
                        .setSuccess(service.deleteById(id, user.getId()))
                );
    }

    @DeleteMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> deleteByUid(@AuthenticationPrincipal final TabUser user,

                                 @PathVariable final <%=id%> id,
                                 @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return new Result<<%=TabName%>>()
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "物理删除数据，带 uid 强校验，也可能会带当前操作人校验，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}/{id【response.data[*].uid】}",
                            "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100, Util.uuid32())))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .setSuccess(service.deleteByUid(id, uid, user.getId()))
                );
    }

    @PatchMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> markDeleteById(@AuthenticationPrincipal final TabUser user,


                                    @PathVariable final <%=id%> id) {
        return new Result<>()
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
                        .call(() -> service.markDeleteById(id, user.getId()))
                );
    }

    @PatchMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> markDeleteByUid(@AuthenticationPrincipal final TabUser user,


                                     @PathVariable final <%=id%> id,
                                     @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return new Result<>()
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "逻辑删除数据，带 uid 强校验，也可能会带当前操作人校验，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}/{id【response.data[*].uid】}",
                            "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100, Util.uuid32())))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .call(() -> service.markDeleteByUid(id, uid, user.getId()))
                );
    }

    @PatchMapping
    @ResponseBody
    @Override
    public Result<?> markDelete(@AuthenticationPrincipal final TabUser user,


                                @RequestBody(required = false) final String body) {
        return new Result<>()
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "逻辑删除数据，带 uid 强校验，也可能会带当前操作人校验，url带参说明:/{version【response.version.id】}",
                            "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()), // 当前接口参考案例请求地址；
                                // 方案1：按 ID 逻辑删除
                                // Arrays.asList(100, 101, 102)
                                // 方案2：按 ID 和 UUID 逻辑删除
                                Arrays.asList(
                                        <%=TabName%>.builder().id(100L).uid(Util.uuid32()).build(),
                                        <%=TabName%>.builder().id(101L).uid(Util.uuid32()).build()
                                )
                        ))
                )
                .execute(result -> result
                        // .call(()->service.markDeleteByIds(Param.of(param).required().hasArray().parseArray(Long.class), user.getId())) // 方案1：按 ID 逻辑删除
                        .call(() -> service.markDelete(Param.of(param).required().hasArray().parseArray(<%=TabName%>.class), user.getId())) // 方案2：按 ID 和 UUID 逻辑删除
                );
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Override
    public Result<?> findById(@AuthenticationPrincipal final TabUser user,
                              @PathVariable final int version,
                              @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id) {
        return new Result<<%=TabName%>>()
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
                        .setSuccess(service.findById(id).orElse(null))
                );
    }

    @GetMapping("/{id}/{uid}")
    @ResponseBody
    @Override
    public Result<?> findByUid(@AuthenticationPrincipal final TabUser user,
                               @PathVariable final int version,

@ApiParam(required = true, value = "数据id", example = "1") @PathVariable final Long id,
                               @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return new Result<<%=TabName%>>()
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                                "按ID + uid 查询单条数据，url带参说明:/{version【response.version.id】}/{id【response.data[*].id】}/{id【response.data[*].uid】}",
                                "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(100, Util.uuid32())))) // 当前接口参考案例请求地址；
                )
                .execute(result -> result
                        .setSuccess(service.findByUid(id, uid).orElse(null))
                );
    }

    @GetMapping
    @ResponseBody
    @Override
    public Result<?> search(
            @AuthenticationPrincipal final TabUser user,


            @RequestParam(required = false, defaultValue = "{}") final String json) {
        return new Result<<%=TabName%>>()
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "查询多条数据，不分页，url带参必须使用 encodeURI 格式化【?json=encodeURI(JSON.stringify({}))】，url带参说明:/{version【response.version.id】}",
                            "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl()), // 当前接口参考案例请求地址；
                                <%=TabName%>.builder() // 当前接口参考案例请求参数，demo中设置支持查询的字段
                                    .deleted(Bool.NO)
                                    .sorts(Collections.singletonList(Sorts.Order.builder().name(OrderBy.id.name()).direction(DESC).build()))
                                    .build()
                        ))
                )
                .execute(result -> result
                        .versionAssert(version, false) // 弱校验版本号
                        .setSuccess(service.findList(
                                Param.of(json).parseObject(<%=TabName%>.class)
                        ))
                );
    }

    @GetMapping("/page/{number}/{size}")
    @ResponseBody
    @Override
    public Result<?> page(
            @AuthenticationPrincipal final TabUser user,


            @ApiParam(required = true, value = "页码", example = "1") @PathVariable final int number,
            @ApiParam(required = true, value = "每页条数", example = "1") @PathVariable final int size,
            @RequestParam(required = false, defaultValue = "{}") final String json
    ) {
        return new Result<<%=TabName%>>()
                .version(this.getClass(), builder -> builder
                        .props(<%=TabName%>.Props.list()) // 当前返回对象属性说明
                        .notes(Arrays.asList( // 当前接口详细说明及版本变更说明
                            "分页查询数据，url带参必须使用 encodeURI 格式化【?json=encodeURI(JSON.stringify({}))】，url带参说明:/{version【response.version.id】}/page/{number【当前页码】}/{size【每页显示条数】}",
                            "1. <%=comment%>"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl(1, 20)), // 当前接口参考案例请求地址；
                                <%=TabName%>.builder() // 当前接口参考案例请求参数，demo中设置支持查询的字段
                                        .deleted(Bool.NO)
                                        .sorts(Collections.singletonList(Sorts.Order.builder().name(OrderBy.id.name()).direction(DESC).build()))
                                        .build()
                        ))
                )
                .execute(result -> result
                        .setSuccess(service.findPage(
                                Param.of(json).parseObject(<%=TabName%>.class),
                                Pager.builder().number(number).size(size).build()
                        ))
                );
    }

}
