import axios from 'axios';
import Page from '../../utils/entity/Page';
import Result from '../../utils/entity/Result';
import Asserts from '../../utils/Asserts';

/**
 * 请求 url 定义
 * @author 谢长春 2019-7-28
 */
const OPEN_DEMO_URL = Object.freeze({
    // save: '', // 新增
    // update: '', // 修改
    // deleteById: '', // 按 id 删除
    // deleteByUid: '', // 按 id + uid 删除
    // markDeleteById: '', // 按 id 逻辑删除
    // markDeleteByUid: '', // 按 id + uid 逻辑删除
    // markDelete: '', // 按 id + uid 批量逻辑删除
    // findById: '', // 按 id 查询单条记录
    // findByIdTimestamp: '', // 按 id + 时间戳 查询单条记录
    // findByUid: '', // // 按 id + uid 查询单条记录
    // findByUidTimestamp: '', // 按 id + uid + 时间戳 查询单条记录
    // search: '', // 多条件批量查询，不分页
    // page: '' // 分页：多条件批量查询
    codes: '/open/test/1/codes', // 查询所有状态码
    save: '/open/test/1', // 新增
    update: '/open/test/1/{id}', // 修改
    deleteById: '/open/test/1/{id}', // 按 id 删除
    markDeleteById: '/open/test/1/{id}', // 按 id 逻辑删除
    markDelete: '/open/test/1', // 按 id + uid 批量逻辑删除
    findById: '/open/test/1/{id}', // 按 id 查询单条记录
    search: '/open/test/1', // 多条件批量查询，不分页
    page: '/open/test/1/{number}/{size}' // 分页：多条件批量查询
});

/**
 * 后台服务请求：参考案例：用于调试后端服务是否可用，以及基本传参格式是否正确，没有数据入库
 * @author 谢长春 2019-7-28
 */
class OpenDemoService {
    /**
     * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
     * @param self {OpenDemoService}
     * @return {OpenDemoService}
     */
    static self(self) {
        return self;
    }

    /**
     * 静态构造函数
     * @param vo {OpenDemoVO} 参考案例对象
     * @return {OpenDemoService}
     */
    static of(vo) {
        return new OpenDemoService(vo);
    }

    /**
     * 静态构造函数
     * @param vo {OpenDemoVO} 参考案例对象
     */
    constructor(vo) {
        Asserts.of().hasFalse(vo, () => 'vo');
        /**
         * 参考案例对象
         * @type {OpenDemoVO}
         */
        this.vo = vo;
    }

    toString() {
        return JSON.stringify(this)
    }

    /**
     * 查询所有状态码
     * @return {Promise<Result>}
     */
    async codes() {
        return await axios
            .get(OPEN_DEMO_URL.codes)
            .then(Result.ofResponse)
            .catch(Result.ofCatch)
    }

    /**
     * 新增
     * @return {Promise<Result>}
     */
    async save() {
        const {name, phone} = this.vo;
        return await axios
            .post(OPEN_DEMO_URL.save, {json: {name, phone}})
            .then(Result.ofResponse)
            .catch(Result.ofCatch);
    }

    /**
     * 修改
     * @return {Promise<Result>}
     */
    async update() {
        const {id, name, phone} = this.vo;
        return await axios
            .put(OPEN_DEMO_URL.update.format(id || 0), {json: {name, phone}})
            .then(Result.ofResponse)
            .catch(Result.ofCatch);
    }

    /**
     * 按 id 删除
     * @return {Promise<Result>}
     */
    async deleteById() {
        const {id} = this.vo;
        return await axios
            .delete(OPEN_DEMO_URL.deleteById.format(id || 0))
            .then(Result.ofResponse)
            .catch(Result.ofCatch);
    }

    /**
     * 按 id 逻辑删除
     * @return {Promise<Result>}
     */
    async markDeleteById() {
        const {id} = this.vo;
        return await axios
            .patch(OPEN_DEMO_URL.markDeleteById.format(id || 0))
            .then(Result.ofResponse)
            .catch(Result.ofCatch);
    }

    /**
     * 按 id + uid 批量逻辑删除
     * @return {Promise<Result>}
     */
    async markDelete() {
        const {ids} = this.vo;
        return await axios
            .patch(OPEN_DEMO_URL.markDelete, {json: ids})
            .then(Result.ofResponse)
            .catch(Result.ofCatch);
    }

    /**
     * 按 id 查询单条记录
     * @return {Promise<Result>}
     */
    async findById() {
        const {id} = this.vo;
        return await axios
            .get(OPEN_DEMO_URL.findById.format(id || 0))
            .then(Result.ofResponse)
            .catch(Result.ofCatch);
    }

    /**
     * 多条件批量查询，不分页
     * @return {Promise<Result>}
     */
    async search() {
        const {id, name, phone} = this.vo;
        return await axios
            .get(OPEN_DEMO_URL.search, {params: {json: {id, name, phone}}})
            .then(Result.ofResponse)
            .catch(Result.ofCatch);
    }

    /**
     * 分页：多条件批量查询
     * @return {Promise<Result>}
     */
    async pageable() {
        const {id, name, phone, page} = this.vo;
        return await axios
            .get(OPEN_DEMO_URL.page.formatObject(page || Page.ofDefault()),
                {params: {json: {id, name, phone}}}
            )
            .then(Result.ofResponse)
            .catch(Result.ofCatch);
    }
}

/**
 * 后台服务请求：参考案例：用于调试后端服务是否可用，以及基本传参格式是否正确，没有数据入库
 * @author 谢长春 2019-7-28
 */
export default class OpenDemoVO {
    /**
     * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
     * @param self {OpenDemoVO}
     * @return {OpenDemoVO}
     */
    static self(self) {
        return self;
    }

    /**
     * 将 result 对象中的 data 集合转换为当前对象集合
     * @param data {Array<object>}
     * @return {Array<OpenDemoVO>}
     */
    static parseList(data) {
        return data.map(OpenDemoVO.of);
    }

    /**
     * 构造参考案例参数
     * @param id {number} 数据 ID
     * @param name {string} 姓名
     * @param phone {string} 手机号
     * @param ids {Array<number>} 批量操作 ids
     * @param page {Page} 分页对象
     * @return {OpenDemoVO}
     */
    static of({id = undefined, name = undefined, phone = undefined, ids = undefined, page = undefined} = {}) {
        return new OpenDemoVO(id, name, phone, ids, page);
    }

    /**
     * 构造参考案例参数：构造函数内部应该列出所有可能的参数，并对参数做说明
     * @param id {number} 数据 ID
     * @param name {string} 姓名
     * @param phone {string} 手机号
     * @param ids {Array<number>} 批量操作 ids
     * @param page {Page} 分页对象
     */
    constructor(id, name, phone, ids, page) {
        /**
         * 数据 ID
         * @type {number}
         */
        this.id = id;
        /**
         * 姓名
         * @type {string}
         */
        this.name = name;
        /**
         * 手机号
         * @type {string}
         */
        this.phone = phone;
        /**
         * 批量操作 ids
         * @type {Array<number>}
         */
        this.ids = ids;
        /**
         * 分页对象
         * @type {Page}
         */
        this.page = page;
    }

    toString() {
        return JSON.stringify(this)
    }

    /**
     * 获取 api 服务对象
     * @return {OpenDemoService}
     */
    getService() {
        return new OpenDemoService(this);
    }
}
