import AuthorityService from '../AuthorityService';

/**
 * 后台服务请求：权限指令
 * @author 谢长春 2019-7-28
 */
export default class Authority {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {Authority}
   * @return {Authority}
   */
  static self(self) {
    return self;
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
   * @param nodes {Array<Authority>} 权限树子节点
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
     * @type {Array<Authority>}
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
