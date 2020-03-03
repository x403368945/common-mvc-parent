package com.support.mvc.web;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

/**
 * 开放式接口规范，无需登录即可访问
 * Controller 基础方法规范接口
 * 注释中有相应的代码实现模板，包括接参规范
 * 每个方法内部代码必须使用 try{}catch(){} 将所有异常变为可枚举的已知异常, 禁止 Controller 方法向外抛出异常
 *
 * @author 谢长春 2017年7月14日 上午11:23:18
 */
@SuppressWarnings("unused")
public interface IController<ID, T> {

    /**
     * <pre>保存:避免 500 之后日志中不打印请求 body，所以先使用 String 接收 body 参数
     * 代码参考：example.DemoListController#save
     * /{version}/{模块url前缀}/{id}
     * </pre>
     *
     * @param body {@link String} body中获取参数；@RequestBody(required = false) String body
     * @return {@link Result}
     */
    @ApiOperationSupport(ignoreParameters = {"body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"})
    default Result<T> save(
            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
            @RequestBody(required = false) final String body) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【save(final String body)】未实现"));
    }

    /**
     * <pre>更新:避免 500 之后日志中不打印请求 body，所以先使用 String 接收 body 参数
     * 代码参考：example.DemoListController#update
     * /{version}/{模块url前缀}/{id}
     * </pre>
     *
     * @param id   ID 数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param body {@link String} body中获取参数；@RequestBody(required = false) String body
     * @return {@link Result}
     */
    @ApiOperationSupport(ignoreParameters = {"body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"})
    default Result<Void> update(
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final ID id,
            // required = false 可以让请求先过来，如果参数为空再抛出异常，保证本次请求能得到响应
            @RequestBody(required = false) final String body) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【update(final ID id, String body)】未实现"));
    }

    /**
     * <pre>按ID删除，物理删除；
     * 代码参考：example.DemoListController#deleteById
     * /{version}/{模块url前缀}/{id}
     * </pre>
     *
     * @param id ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @return {@link Result}
     */
    default Result<Void> deleteById(@ApiParam(required = true, value = "数据id", example = "1") @PathVariable final ID id) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【deleteById(final ID id)】未实现"));
    }

    /**
     * <pre>按 ID 和 UUID 删除，物理删除；
     * 代码参考：example.DemoListController#deleteByUid
     * /{version}/{模块url前缀}/{id}/{uid}
     * </pre>
     *
     * @param id  ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid {@link String} 数据UUID 请求url中获取当前请求数据UUID；@ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid
     * @return {@link Result}
     */
    default Result<Void> deleteByUid(
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final ID id,
            @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【deleteByUid(final ID id, final String uid)】未实现"));
    }

    /**
     * <pre>按ID删除，逻辑删除
     * 代码参考：example.DemoListController#markDeleteById
     * /{version}/{模块url前缀}/{id}
     *
     * @param id      ID 数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @return {@link Result}
     */
    default Result<Void> markDeleteById(@ApiParam(required = true, value = "数据id", example = "1") @PathVariable final ID id) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【markDeleteById(final ID id)】未实现"));
    }

    /**
     * <pre>按 ID 和 UUID 删除，逻辑删除；
     * 代码参考：example.DemoListController#markDeleteByUid
     * /{version}/{模块url前缀}/{id}/{uid}
     * </pre>
     *
     * @param id  ID 数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid {@link String} 数据UUID 请求url中获取当前请求数据UUID；@ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid
     * @return {@link Result}
     */
    default Result<Void> markDeleteByUid(
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final ID id,
            @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【markDeleteByUid(final ID id, final String uid)】未实现"));
    }

    /**
     * <pre>批量操作按 ID，逻辑删除；
     * /{version}/{模块url前缀}
     * </pre>
     *
     * @param body {@link Set} body中获取参数；@RequestBody(required = false) String body
     * @return {@link Result}
     */
    default Result<Void> markDeleteByIds(@ApiParam(value = "数据 id 集合") @RequestBody final Set<ID> body) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【markDelete(final Set<ID> body)】未实现"));
    }

    /**
     * <pre>批量操作按 ID 和 UUID 删除，逻辑删除；
     * 代码参考：example.DemoListController#markDelete
     * /{version}/{模块url前缀}
     * </pre>
     *
     * @param body {@link List} body中获取参数；@RequestBody(required = false) T body
     * @return {@link Result}
     */
    default Result<Void> markDelete(@ApiParam(value = "数据 id、uid 集合") @RequestBody final List<MarkDelete> body) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【markDelete(final List<MarkDelete> body)】未实现"));
    }

    /**
     * <pre>按ID查询；
     * 代码参考：example.DemoListController#findById
     * /{version}/{模块url前缀}/{id}
     * </pre>
     *
     * @param id ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @return {@link Result}
     */
    default Result<? extends T> findById(@ApiParam(required = true, value = "数据id", example = "1") @PathVariable final ID id) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【findById(final ID id)】未实现"));
    }

    /**
     * <pre>按ID和UUID查询
     * 代码参考：example.DemoListController#findByUid
     * /{version}/{模块url前缀}/{id}/{uid}
     * </pre>
     *
     * @param id  ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid {@link String} 数据UUID 请求url中获取当前请求数据UUID；@ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid
     * @return {@link Result}
     */
    default Result<? extends T> findByUid(
            @ApiParam(required = true, value = "数据id", example = "1") @PathVariable final ID id,
            @ApiParam(required = true, value = "数据uid", example = "uuid32") @PathVariable final String uid) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【findByUid(final ID id, final String uid)】未实现"));
    }

    /**
     * <pre>按条件查询列表，不分页；url带参需要 URLEncoder 格式化
     * 代码参考：example.DemoListController#search
     * /{version}/{模块url前缀}
     * </pre>
     *
     * @param condition {@link T}
     * @return {@link Result}
     */
    default <P extends T> Result<? extends T> search(final P condition) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【search(final P condition)】未实现"));
    }

    /**
     * <pre>按条件分页查询列表；url带参需要 URLEncoder 格式化
     * 代码参考：example.DemoListController#page
     * /{version}/{模块url前缀}/page/{number}/{size}
     * </pre>
     *
     * @param number    int 页码
     * @param size      int 每页大小
     * @param condition {@link T}
     * @return {@link Result}
     */
    default <P extends T> Result<? extends T> page(
            @ApiParam(required = true, value = "页码", example = "1") @PathVariable final int number,
            @ApiParam(required = true, value = "每页条数", example = "10") @PathVariable final int size,
            final P condition) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【search(final int number, final int size, final P condition)】未实现"));
    }
}