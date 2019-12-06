import axios from 'axios';
import Page from '../utils/entity/Page';
import Result from '../utils/entity/Result';
import Asserts from '../utils/entity/Asserts';

/**
 * 请求 url 定义
 * @author 谢长春 2019-8-30
 */
const AUTHORITY_URL = Object.freeze({
  list: '/authority/1/list', // 权限指令列表
  tree: '/authority/1/tree', // 权限指令树
});

/**
 * 后台服务请求：权限指令
 * @author 谢长春 2019-7-28
 */
export class AuthorityService {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {AuthorityService}
   * @return {AuthorityService}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @param vo {AuthorityVO} 参考案例对象
   * @return {AuthorityService}
   */
  static of(vo) {
    return new AuthorityService(vo);
  }

  /**
   * 静态构造函数
   * @param vo {AuthorityVO} 参考案例对象
   */
  constructor(vo) {
    Asserts.of().hasFalse(vo, () => 'vo');
    /**
     * 参考案例对象
     * @type {AuthorityVO}
     */
    this.vo = vo;
  }

  toString() {
    return JSON.stringify(this)
  }

  /**
   * 查询权限指令树
   * @return {Promise<Result>}
   */
  async getTree() {
    return await axios
      .get(AUTHORITY_URL.tree)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 查询权限指令列表
   * @return {Promise<Result>}
   */
  async getList() {
    return await axios
      .get(AUTHORITY_URL.list)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

}

/**
 * 后台服务请求：权限指令
 * @author 谢长春 2019-7-28
 */
export default class AuthorityVO {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {AuthorityVO}
   * @return {AuthorityVO}
   */
  static self(self) {
    return self;
  }

  /**
   * 将 result 对象中的 data 集合转换为当前对象集合
   * @param data {Array<object>}
   * @return {Array<AuthorityVO>}
   */
  static parseList(data) {
    return data.map(AuthorityVO.of);
  }

  /**
   * 构造函数：内部应该列出所有可能的参数，并对参数做说明
   * @param code {string} 权限指令代码
   * @param parentCode {string} 父级权限指令代码
   * @param name {string} 名称
   * @param icon {string} 图标
   * @param type {string} 权限类型
   * @param route {string} 菜单路由地址 route，可选参数，需要跟前端约定
   * @param checked {boolean} 权限是否被选中
   * @param nodes {Array<AuthorityVO>} 权限树子节点
   */
  constructor({
                code = undefined,
                parentCode = undefined,
                name = undefined,
                icon = undefined,
                type = undefined,
                route = undefined,
                checked = undefined,
                nodes = undefined
              } = {}) {
    /**
     * 权限指令代码
     * @type {string}
     */
    this.code = code;
    /**
     * 父级权限指令代码
     * @type {string}
     */
    this.parentCode = parentCode;
    /**
     * 名称
     * @type {string}
     */
    this.name = name;
    /**
     * 图标
     * @type {string}
     */
    this.icon = icon;
    /**
     * 权限类型
     * @type {string}
     */
    this.type = type;
    /**
     * 菜单路由地址 route，可选参数，需要跟前端约定
     * @type {string}
     */
    this.route = route;
    /**
     * 权限是否被选中
     * @type {boolean}
     */
    this.checked = checked;
    /**
     * 权限树子节点
     * @type {Array<AuthorityVO>}
     */
    this.nodes = nodes;

  }

  toString() {
    return JSON.stringify(this)
  }

  /**
   * 获取 api 服务对象
   * @return {AuthorityService}
   */
  getService() {
    return new AuthorityService(this);
  }
}
