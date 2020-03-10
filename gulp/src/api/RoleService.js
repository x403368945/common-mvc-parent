import axios from 'axios';
import Page from '../utils/entity/Page';
import Result from '../utils/entity/Result';
import RoleVO from './entity/Role';

/**
 * 请求 url 定义
 * @author 谢长春 2019-8-30
 */
const saveURL = '/1/role'; // 新增
const updateURL = '/1/role/{id}'; // 修改
const markDeleteByUidURL = '/1/role/{id}/{uid}'; // 按 id + uid 逻辑删除
const markDeleteURL = '/1/role'; // 按 id + uid 批量逻辑删除
const findByUidURL = '/1/role/{id}/{uid}'; // 按 id + ui 查询单条记录
const pageURL = '/1/role/page/{number}/{size}'; // 分页：多条件批量查询
const optionsURL = '/1/role/options';// 角色下拉列表选项

/**
 * 后台服务请求：角色
 * @author 谢长春 2019-7-28
 */
export default class RoleService {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {RoleService}
   * @return {RoleService}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @param vo {RoleVO} 参考案例对象
   * @return {RoleService}
   */
  static of(vo) {
    return new RoleService(vo);
  }

  /**
   * 静态构造函数
   * @param vo {RoleVO} 参考案例对象
   */
  constructor(vo) {
    let vobject = null;
    if (vo) {
      vobject = new RoleVO({...vo});
      Object.keys(vobject).forEach(key => { // 移除空字符串参数，前端组件默认值为空字符串，带到后端查询会有问题
        if (vobject[key] === '') {
          delete vobject[key];
        }
      })
    }
    /**
     * 参考案例对象
     * @type {RoleVO}
     */
    this.vo = vo || new RoleVO({});
  }

  toString() {
    return JSON.stringify(this)
  }

  /**
   * 新增
   * @return {Promise<Result>}
   */
  async save() {
    const {name, authorityTree} = this.vo;
    return await axios
      .post(saveURL, {
        name,
        authorityTree
      })
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
    const {markDeleteArray} = this.vo;
    return await axios
      .patch(markDeleteURL, markDeleteArray)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid 查询单条记录
   * @return {Promise<Result>}
   */
  async findByUid() {
    const {id, uid} = this.vo;
    return await axios
      .get(findByUidURL.format(id || 0, uid))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 分页：多条件批量查询
   * @return {Promise<Result>}
   */
  async pageable() {
    const {page, markDeleteArray, ...params} = this.vo;
    return await axios
      .get(pageURL.formatObject(page || Page.ofDefault()), {params})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 角色下拉列表选项
   * @return {Promise<Result>}
   */
  async options() {
    return await axios
      .get(optionsURL)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }
}
