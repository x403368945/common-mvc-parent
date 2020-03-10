import axios from 'axios';
import Page from '../utils/entity/Page';
import Result from '../utils/entity/Result';
import OpenDemo from './entity/OpenDemo';

/**
 * 请求 url 定义
 * @author 谢长春 2019-7-28
 */
// save: '', // 新增
// update: '', // 修改
// deleteById: '', // 按 id 删除
// deleteByUid: '', // 按 id + uid 删除
// markDeleteById: '', // 按 id 逻辑删除
// markDeleteByUid: '', // 按 id + uid 逻辑删除
// markDelete: '', // 按 id + uid 批量逻辑删除
// findById: '', // 按 id 查询单条记录
// findByUid: '', // // 按 id + uid 查询单条记录
// findByUidTimestamp: '', // 按 id + uid + 时间戳 查询单条记录
// search: '', // 多条件批量查询，不分页
// page: '' // 分页：多条件批量查询
const codesURL = '/1/open/test/codes';// 查询所有状态码
const saveURL = '/1/open/test';// 新增
const updateURL = '/1/open/test/{id}';// 修改
const deleteByIdURL = '/1/open/test/{id}';// 按 id 删除
const markDeleteByIdURL = '/1/open/test/{id}';// 按 id 逻辑删除
const markDeleteURL = '/1/open/test';// 按 id + uid 批量逻辑删除
const findByIdURL = '/1/open/test/{id}';// 按 id 查询单条记录
const searchURL = '/1/open/test';// 多条件批量查询，不分页
const pageURL = '/1/open/test/{number}/{size}';// 分页：多条件批量查询

/**
 * 后台服务请求：参考案例：用于调试后端服务是否可用，以及基本传参格式是否正确，没有数据入库
 * @author 谢长春 2019-7-28
 */
export default class OpenDemoService {
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
   * @param vo {OpenDemo} 参考案例对象
   * @return {OpenDemoService}
   */
  static of(vo) {
    return new OpenDemoService(vo);
  }

  /**
   * 静态构造函数
   * @param vo {OpenDemo} 参考案例对象
   */
  constructor(vo) {
    let vobject = null;
    if (vo) {
      vobject = new OpenDemo({...vo});
      Object.keys(vobject).forEach(key => { // 移除空字符串参数，前端组件默认值为空字符串，带到后端查询会有问题
        if (vobject[key] === '') {
          delete vobject[key];
        }
      })
    }
    /**
     * 参考案例对象
     * @type {OpenDemo}
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
      .get(codesURL)
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
      .post(saveURL, {
        json: {
          name,
          phone
        }
      })
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
      .put(updateURL.format(id || 0), {
        json: {
          name,
          phone
        }
      })
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
   * 按 id + uid 批量逻辑删除
   * @return {Promise<Result>}
   */
  async markDelete() {
    const {ids} = this.vo;
    return await axios
      .patch(markDeleteURL, {json: ids})
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
      .get(findByIdURL.format(id || 0))
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
      .get(searchURL, {
        params: {
          json: {
            id,
            name,
            phone
          }
        }
      })
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
      .get(pageURL.formatObject(page || Page.ofDefault()),
        {
          params: {
            json: {
              id,
              name,
              phone
            }
          }
        }
      )
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }
}
