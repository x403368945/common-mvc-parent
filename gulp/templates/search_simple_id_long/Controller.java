package <%=pkg%>.code.<%=javaname%>.web;

import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>;
import <%=pkg%>.code.<%=javaname%>.service.<%=JavaName%>Service;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import com.support.mvc.web.IController;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.ApiParam;
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
//@ApiSort(1) // 控制接口排序
@RequestMapping("/<%=java_name%>/{version}")
@Controller
@Slf4j
@RequiredArgsConstructor
public class <%=JavaName%>Controller implements IController<String, <%=TabName%>> {

    private final <%=JavaName%>Service service;

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_find')")
    @ApiOperation(value = "1.按 id 查询<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(order = 1) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<?> findById(final String id) {
        return new Result<<%=TabName%>>().execute(result -> result.setSuccess(service.findById(id).orElse(null)));
    }

/*
    @GetMapping("/{id}/{uid}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_find')")
    @ApiOperation(value = "5.按 id 和 uid 查询<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(order = 5) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<TabRole> findByUid(final String id, final String uid) {
        return new Result<TabRole>().execute(result -> result.setSuccess(service.findByUid(id, uid).orElse(null)));
    }
*/
    @GetMapping("/page/{number}/{size}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_page')")
    @ApiOperation(value = "2.分页查询<%=comment%>", tags = {"<%=date%>"})
    @ApiOperationSupport(
            order = 2,
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
/*
}
