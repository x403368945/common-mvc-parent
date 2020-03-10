import axios from 'axios';
import Page from '../utils/entity/Page';
import Result from '../utils/entity/Result';
import DemoList from './entity/DemoList';

const saveURL = '/1/demo-list'; // 新增
const updateURL = '/1/demo-list/{id}'; // 修改
const deleteByIdURL = '/1/demo-list/{id}'; // 按 id 删除
const deleteByUidURL = '/1/demo-list/{id}/{uid}'; // 按 id + uid 删除
const markDeleteByIdURL = '/1/demo-list/{id}'; // 按 id 逻辑删除
const markDeleteByUidURL = '/1/demo-list/{id}/{uid}'; // 按 id + uid 逻辑删除
const markDeleteURL = '/1/demo-list'; // 按 id + uid 批量逻辑删除
const findByIdURL = '/1/demo-list/{id}'; // 按 id 查询单条记录
const findByUidURL = '/1/demo-list/{id}/{uid}'; // 按 id + uid + 时间戳 查询单条记录
const searchURL = '/1/demo-list'; // 多条件批量查询，不分页
const pageURL = '/1/demo-list/page/{number}/{size}'; // 分页：多条件批量查询

/**
 * 后台服务请求：参考案例：实体表操作
 * @author 谢长春 2019-7-28
 */
export default class DemoListService {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {DemoListService}
   * @return {DemoListService}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @param vo {DemoList} 参考案例对象
   * @return {DemoListService}
   */
  static of(vo) {
    return new DemoListService(vo);
  }

  /**
   * 静态构造函数
   * @param vo {DemoList} 参考案例对象
   */
  constructor(vo = {}) {
    /**
     * 参考案例对象
     * @type {DemoList}
     */
    this.vo = new DemoList({
      ...vo
    });
    Object.keys(this.vo).forEach(key => { // 移除空字符串参数，前端组件默认值为空字符串，带到后端查询会有问题
      if (this.vo[key] === '') {
        delete this.vo[key];
      }
    });
  }

  /**
   * 新增
   * @return {Promise<Result>}
   */
  async save() {
    return await axios
      .post(saveURL, this.vo)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 修改
   * @return {Promise<Result>}
   */
  async update() {
    const {id, ...body} = this.vo;
    return await axios
      .put(updateURL.format(id || 0), body)
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
      .delete(deleteByIdURL.format(id || 0))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid 删除
   * @return {Promise<Result>}
   */
  async deleteByUid() {
    const {id, uid} = this.vo;
    return await axios
      .delete(deleteByUidURL.format(id || 0, uid))
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
      .patch(markDeleteByIdURL.format(id || 0))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid 逻辑删除
   * @return {Promise<Result>}
   */
  async markDeleteByUid() {
    const {id, uid} = this.vo;
    return await axios
      .patch(markDeleteByUidURL.format(id || 0, uid))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid 批量逻辑删除
   * @return {Promise<Result>}
   */
  async markDelete() {
    const {uids} = this.vo;
    return await axios
      .patch(markDeleteURL, uids)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + 时间戳 查询单条记录
   * @return {Promise<Result>}
   */
  async findById() {
    const {id, timestamp} = this.vo;
    return await axios
      .get(findByIdURL.format(id || 0, timestamp))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid + 时间戳 查询单条记录
   * @return {Promise<Result>}
   */
  async findByUid() {
    const {id, uid, timestamp} = this.vo;
    return await axios
      .get(findByUidURL.format(id || 0, uid, timestamp))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 多条件批量查询，不分页
   * @return {Promise<Result>}
   */
  async search() {
    return await axios
      .get(searchURL, {params: this.vo})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 分页：多条件批量查询
   * @return {Promise<Result>}
   */
  async pageable() {
    const {page, ...params} = this.vo;
    return await axios
      .get(pageURL.formatObject(page || Page.ofDefault()), {params})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }
}
