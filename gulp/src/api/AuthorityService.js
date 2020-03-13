import axios from 'axios';
import Result from '../utils/entity/Result';
import Authority from './entity/Authority';
import pickBy from 'lodash/pickBy';
import isArray from 'lodash/isArray';
import isObject from 'lodash/isObject';

const listURL = '/1/authority/list'; // 权限指令列表
const treeURL = '/1/authority/tree'; // 权限指令树

/**
 * 后台服务请求：权限指令
 * @author 谢长春 2019-7-28
 */
export default class AuthorityService {
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
   * @param vo {Authority} 参考案例对象
   * @return {AuthorityService}
   */
  static of(vo) {
    return new AuthorityService(vo);
  }

  /**
   * 静态构造函数
   * @param vo {Authority} 参考案例对象
   */
  constructor(vo = {}) {
    /**
     * 参考案例对象
     * @type {Authority}
     */
    this.vo = new Authority(pickBy(JSON.parse(JSON.stringify(vo)), value => value !== ''));
    Object.keys(this.vo).forEach(key => { // 移除空字符串参数，前端组件默认值为空字符串，带到后端会有问题
      if (isArray(this.vo[key])) {
        this.vo[key] = this.vo[key].filter(item => item !== '');
        this.vo[key].forEach(item => {
          if (!isArray(item) && isObject(item)) {
            pickBy(item, value => value !== '');
          }
        })
      }
    });
  }

  /**
   * 查询权限指令树
   * @return {Promise<Result>}
   */
  async getTree() {
    return await axios
      .get(treeURL)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 查询权限指令列表
   * @return {Promise<Result>}
   */
  async getList() {
    return await axios
      .get(listURL)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }
}
