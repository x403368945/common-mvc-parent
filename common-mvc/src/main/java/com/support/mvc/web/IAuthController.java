package com.support.mvc.web;

import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;

/**
 * 封闭式接口规范，登校验当前操作用户
 * Controller 基础方法规范接口
 * 注释中有相应的代码实现模板，包括接参规范
 * 每个方法内部代码必须使用 try{}catch(){} 将所有异常变为可枚举的已知异常, 禁止 Controller 方法向外抛出异常
 *
 * @author 谢长春 2017年7月14日 上午11:23:18
 */
public interface IAuthController<USER, ID> {

    /**
     * 保存:
     * /{模块url前缀}/{version}
     *
     * @param user    {@link USER} 会话中获取用户信息；@AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号；@PathVariable final int version
     * @param body    {@link String} body中获取参数；@RequestBody(required = false) String body
     * @return {@link Result}
     */
    default Result<?> save(final USER user, final int version, final String body) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【save(final USER user, final int version, final String body)】未实现"));
    }

    /**
     * 更新:
     * /{模块url前缀}/{version}/{id}
     *
     * @param user    {@link USER} 会话中获取用户信息；@AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号；@PathVariable final int version
     * @param id      ID 数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param body    {@link String} body中获取参数；@RequestBody(required = false) String body
     * @return {@link Result}
     */
    default Result<?> update(final USER user, final int version, final ID id, String body) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【update(final USER user, final int version, final ID id, String body)】未实现"));
    }

    /**
     * 按ID删除，物理删除:
     * /{模块url前缀}/{version}/{id}
     *
     * @param user    {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @return {@link Result}
     */
    default Result<?> deleteById(final USER user, final int version, final ID id) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【deleteById(final USER user, final int version, final ID id)】未实现"));
    }

    /**
     * 按 ID 和 UUID 删除，物理删除:
     * /{模块url前缀}/{version}/{id}/{uid}
     *
     * @param user    {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid     {@link String} 数据UUID 请求url中获取当前请求数据UUID；@PathVariable final String uid
     * @return {@link Result}
     */
    default Result<?> deleteByUid(final USER user, final int version, final ID id, final String uid) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【deleteByUid(final USER user, final int version, final ID id, final String uid)】未实现"));
    }

    /**
     * 按ID删除，逻辑删除
     * /{模块url前缀}/{version}/{id}
     *
     * @param user    {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID 数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @return {@link Result}
     */
    default Result<?> markDeleteById(final USER user, final int version, final ID id) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【markDeleteById(final USER user, final int version, final ID id)】未实现"));
    }

    /**
     * 按 ID 和 UUID 删除，逻辑删除
     * /{模块url前缀}/{version}/{id}/{uid}
     *
     * @param user    {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID 数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid     {@link String} 数据UUID 请求url中获取当前请求数据UUID；@PathVariable final String uid
     * @return {@link Result}
     */
    default Result<?> markDeleteByUid(final USER user, final int version, final ID id, final String uid) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【markDeleteByUid(final USER user, final int version, final ID id, final String uid)】未实现"));
    }

    /**
     * 批量操作按 ID 和 UUID 删除，逻辑删除
     * /{模块url前缀}/{version}
     *
     * @param user    {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param body    {@link String} body中获取参数；@RequestBody(required = false) String body
     * @return {@link Result}
     */
    default Result<?> markDelete(final USER user, final int version, final String body) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【markDelete(final USER user, final int version, final String body)】未实现"));
    }

    /**
     * 按ID查询
     * /{模块url前缀}/{version}/{id}
     * 建议优先使用 {@link IAuthController#findByIdTimestamp(USER, int, Object, long)} 方案，该方案可以适配缓存兼容
     *
     * @param user    {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @return {@link Result}
     */
    default Result<?> findById(final USER user, final int version, final ID id) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【findById(final USER user, final int version, final ID id)】未实现"));
    }

    /**
     * 按ID查询
     * /{模块url前缀}/{version}/{id}
     *
     * @param user      {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version   {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id        ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param timestamp {@link Long} 数据最后一次更新时间戳；方便兼容缓存方案
     * @return {@link Result}
     */
    default Result<?> findByIdTimestamp(final USER user, final int version, final ID id, final long timestamp) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【findById(final USER user, final int version, final ID id, final Timestamp timestamp)】未实现"));
    }

    /**
     * 按ID和UUID查询
     * /{模块url前缀}/{version}/{id}/{uid}
     * 建议优先使用 {@link IAuthController#findByUidTimestamp(USER, int, Object, String, long)} 方案，该方案可以适配缓存兼容
     *
     * @param user    {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id      ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid     {@link String} 数据UUID 请求url中获取当前请求数据UUID；@PathVariable final String uid
     * @return {@link Result}
     */
    default Result<?> findByUid(final USER user, final int version, final ID id, final String uid) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【findByUid(final USER user, final int version, final ID id, final String uid)】未实现"));
    }

    /**
     * 按ID和UUID查询
     * /{模块url前缀}/{version}/{id}/{uid}
     *
     * @param user      {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version   {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param id        ID  数据ID 请求url中获取当前请求数据ID；@PathVariable final ID id
     * @param uid       {@link String} 数据UUID 请求url中获取当前请求数据UUID；@PathVariable final String uid
     * @param timestamp {@link Long} 数据最后一次更新时间戳；方便兼容缓存方案
     * @return {@link Result}
     */
    default Result<?> findByUidTimestamp(final USER user, final int version, final ID id, final String uid, final long timestamp) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【findByUid(final USER user, final int version, final ID id, final String uid, final Timestamp timestamp)】未实现"));
    }

    /**
     * 按条件查询列表，不分页；url带参需要 URLEncoder 格式化
     * /{模块url前缀}/{version}
     *
     * @param user    {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param json    {@link String} TODO 这里小心入坑注意看注释；@RequestParam 与 @RequestBody 不同；不能自动将 json 字符串转换为对象，所以这里需要用 String 接收前端传递的json字符串对象；
     * @return {@link Result}
     */
    default Result<?> search(final USER user, final int version, final String json) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【search(final USER user, final int version, final String json)】未实现"));
    }

    /**
     * 按条件分页查询列表；url带参需要 URLEncoder 格式化
     * /{模块url前缀}/{version}/page/{number}/{size}
     *
     * @param user    {@link USER} 会话中获取用户信息； @AuthenticationPrincipal USER user
     * @param version {@link Integer} 请求url中获取当前请求接口版本号； @PathVariable final int version
     * @param number  int 页码
     * @param size    int 每页大小
     * @param json    {@link String} TODO 这里小心入坑注意看注释； @RequestParam 与 @RequestBody 不同；不能自动将 json 字符串转换为对象，所以这里需要用 String 接收前端传递的json字符串对象
     * @return {@link Result}
     */
    default Result<?> page(final USER user, final int version, final int number, final int size, final String json) {
        return Code.FAILURE.toResult(this.getClass().getName().concat("：方法【search(final USER user, final int version, final int number, final int size, final String json)】未实现"));
    }
}