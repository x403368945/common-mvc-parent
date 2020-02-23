package com.support.mvc.web;

import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;

/**
 * 开放式接口规范，无需登录即可访问
 * Controller 基础方法规范接口
 * 注释中有相应的代码实现模板，包括接参规范
 * 每个方法内部代码必须使用 try{}catch(){} 将所有异常变为可枚举的已知异常, 禁止 Controller 方法向外抛出异常
 *
 * @author 谢长春 2017年7月14日 上午11:23:18
 */
public interface IController<ID> {

    /**
     * <pre>保存；
     * 代码参考：example.DemoListController#save
     * /{模块url前缀}/{version}/{id}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号；@PathVariable final int version
     * @param body  {@link String} body中获取参数；@RequestBody(required = false) String body
     * @return {@link Result}
     */
    default Result<?> save(final int version, final String body) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【save(final int version, final String body)】未实现"));
    }

    /**
     * <pre>更新；
     * 代码参考：example.DemoListController#update
     * /{模块url前缀}/{version}/{id}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号；@PathVariable final int version
     * @param id      ID 数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param body  {@link String} body中获取参数；@RequestBody(required = false) String body
     * @return {@link Result}
     */
    default Result<?> update(final int version, final ID id, String body) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【update(final int version, final ID id, String body)】未实现"));
    }

    /**
     * <pre>按ID删除，物理删除；
     * 代码参考：example.DemoListController#deleteById
     * /{模块url前缀}/{version}/{id}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @return {@link Result}
     */
    default Result<?> deleteById(final int version, final ID id) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【deleteById(final int version, final ID id)】未实现"));
    }

    /**
     * <pre>按 ID 和 UUID 删除，物理删除；
     * 代码参考：example.DemoListController#deleteByUid
     * /{模块url前缀}/{version}/{id}/{uid}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid     {@link String} 数据UUID 请求url中获取当前请求数据UUID；@PathVariable final String uid
     * @return {@link Result}
     */
    default Result<?> deleteByUid(final int version, final ID id, final String uid) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【deleteByUid(final int version, final ID id, final String uid)】未实现"));
    }

    /**
     * <pre>按ID删除，逻辑删除
     * 代码参考：example.DemoListController#markDeleteById
     * /{模块url前缀}/{version}/{id}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID 数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @return {@link Result}
     */
    default Result<?> markDeleteById(final int version, final ID id) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【markDeleteById(final int version, final ID id)】未实现"));
    }

    /**
     * <pre>按 ID 和 UUID 删除，逻辑删除；
     * 代码参考：example.DemoListController#markDeleteByUid
     * /{模块url前缀}/{version}/{id}/{uid}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID 数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid     {@link String} 数据UUID 请求url中获取当前请求数据UUID；@PathVariable final String uid
     * @return {@link Result}
     */
    default Result<?> markDeleteByUid(final int version, final ID id, final String uid) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【markDeleteByUid(final int version, final ID id, final String uid)】未实现"));
    }

    /**
     * <pre>批量操作按 ID 和 UUID 删除，逻辑删除；
     * 代码参考：example.DemoListController#markDelete
     * /{模块url前缀}/{version}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param body  {@link String} body中获取参数；@RequestBody(required = false) String body
     * @return {@link Result}
     */
    default Result<?> markDelete(final int version, final String body) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【markDelete(final int version, final String body)】未实现"));
    }

    /**
     * <pre>按ID查询；
     * 建议优先使用 {@link IController#findByIdTimestamp(int, Object, long)} 方案，该方案可以适配缓存兼容
     * 代码参考：example.DemoListController#findById
     * /{模块url前缀}/{version}/{id}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @return {@link Result}
     */
    default Result<?> findById(final int version, final ID id) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【findById(final int version, final ID id)】未实现"));
    }

    /**
     * <pre>按ID查询；
     * 代码参考：example.DemoListController#findByIdTimestamp
     * /{模块url前缀}/{version}/{id}/{timestamp}
     *
     * @param version   {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id        ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param timestamp {@link Long} 数据最后一次更新时间戳；方便兼容缓存方案
     * @return {@link Result}
     */
    default Result<?> findByIdTimestamp(final int version, final ID id, final long timestamp) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【findById(final int version, final ID id, final Timestamp timestamp)】未实现"));
    }

    /**
     * <pre>按ID和UUID查询
     * 建议优先使用 {@link IController#findByUidTimestamp(int, Object, String, long)} 方案，该方案可以适配缓存兼容
     * 代码参考：example.DemoListController#findByUid
     * /{模块url前缀}/{version}/{id}/{uid}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid     {@link String} 数据UUID 请求url中获取当前请求数据UUID；@PathVariable final String uid
     * @return {@link Result}
     */
    default Result<?> findByUid(final int version, final ID id, final String uid) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【findByUid(final int version, final ID id, final String uid)】未实现"));
    }

    /**
     * <pre>按ID和UUID查询
     * 代码参考：example.DemoListController#findByUidTimestamp
     * /{模块url前缀}/{version}/{id}/{uid}
     *
     * @param version   {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id        ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid       {@link String} 数据UUID 请求url中获取当前请求数据UUID；@PathVariable final String uid
     * @param timestamp {@link Long} 数据最后一次更新时间戳；方便兼容缓存方案
     * @return {@link Result}
     */
    default Result<?> findByUidTimestamp(final int version, final ID id, final String uid, final long timestamp) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【findByUid(final int version, final ID id, final String uid, final Timestamp timestamp)】未实现"));
    }

    /**
     * <pre>按条件查询列表，不分页；url带参需要 URLEncoder 格式化
     * 代码参考：example.DemoListController#search
     * /{模块url前缀}/{version}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param json    {@link String} TODO 这里小心入坑注意看注释；@RequestParam 与 @RequestBody 不同；不能自动将 json 字符串转换为对象，所以这里需要用 String 接收前端传递的json字符串对象；
     * @return {@link Result}
     */
    default Result<?> search(final int version, final String json) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【search(final int version, final String json)】未实现"));
    }

    /**
     * <pre>按条件分页查询列表；url带参需要 URLEncoder 格式化
     * 代码参考：example.DemoListController#page
     * /{模块url前缀}/{version}/page/{number}/{size}
     *
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param number  int 页码
     * @param size    int 每页大小
     * @param json    {@link String} TODO 这里小心入坑注意看注释； @RequestParam 与 @RequestBody 不同；不能自动将 json 字符串转换为对象，所以这里需要用 String 接收前端传递的json字符串对象
     * @return {@link Result}
     */
    default Result<?> page(final int version, final int number, final int size, final String json) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【search(final int version, final int number, final int size, final String json)】未实现"));
    }
}