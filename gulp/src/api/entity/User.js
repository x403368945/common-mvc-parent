import UserService from '../UserService';

/**
 * 用户实体对象
 * @author 谢长春 2019-7-28
 */
export default class User {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {User}
   * @return {User}
   */
  static self(self) {
    return self;
  }

  /**
   * 构造参考案例参数
   * @return {User}
   */
  static of(obj) {
    return new User(obj || {});
  }

  /**
   * 构造参考案例参数：构造函数内部应该列出所有可能的参数，并对参数做说明
   * @param id {number} 数据 ID
   * @param uid {string} 用户UUID，缓存和按ID查询时可使用强校验
   * @param username {string} 登录名
   * @param password {string} 登录密码
   * @param nickname {string} 用户昵称
   * @param phone {string} 手机号
   * @param email {string} 邮箱
   * @param roles {Array<number>} 角色 ID 集合
   * @param domain {string} 该属性目前只有测试时使用，常规接口不用传值，测试接口传 test
   * @param roleList {Array<RoleVO>} 新增用户时，选择的角色集合，经过验证之后，将角色 ID 保存到 roles
   * @param authorityList {Array<string>} 角色对应的权限指令集合
   * @param roleId {number} 角色 id {@link RoleVO#id}，按角色查询用户列表时使用该字段
   * @param sorts {Array<OrderBy>} 排序字段集合
   * @param page {Page} 分页对象
   * @param markDeleteArray {Array<MarkDelete>} 批量删除
   */
  constructor({
                id = undefined,
                uid = undefined,
                username = undefined,
                password = undefined,
                nickname = undefined,
                phone = undefined,
                email = undefined,
                roles = undefined,
                domain = undefined,
                roleList = undefined,
                authorityList = undefined,
                roleId = undefined,
                sorts = undefined,
                page = undefined,
                markDeleteArray = undefined
              } = {}) {
    /**
     * 数据 ID
     * @type {number}
     */
    this.id = id;
    /**
     * 用户UUID，缓存和按ID查询时可使用强校验
     * @type {string}
     */
    this.uid = uid;
    /**
     * 登录名
     * @type {string}
     */
    this.username = username;
    /**
     * 登录密码
     * @type {string}
     */
    this.password = password;
    /**
     * 用户昵称
     * @type {string}
     */
    this.nickname = nickname;
    /**
     * 手机号
     * @type {string}
     */
    this.phone = phone;
    /**
     * 邮箱
     * @type {string}
     */
    this.email = email;
    /**
     * 角色 ID 集合
     * @type {Array<number>}
     */
    this.roles = roles;
    /**
     * 该属性目前只有测试时使用，常规接口不用传值，测试接口传 test
     * @type {string}
     */
    this.domain = domain;
    /**
     * 新增用户时，选择的角色集合，经过验证之后，将角色 ID 保存到 roles
     * @type {Array<RoleVO>}
     */
    this.roleList = roleList;
    /**
     * 角色对应的权限指令集合
     * @type {Array<string>}
     */
    this.authorityList = authorityList;
    /**
     * 角色 id {@link RoleVO#id}
     * @type {number}
     */
    this.roleId = roleId;
    /**
     * 排序字段集合
     * @type {Array<OrderBy>}
     */
    this.sorts = sorts;
    /**
     * 分页对象
     * @type {Page}
     */
    this.page = page;
    /**
     * 批量删除
     * @type {Array<MarkDelete>}
     */
    this.markDeleteArray = markDeleteArray;
  }

  toString() {
    return JSON.stringify(this)
  }

  /**
   * 获取 api 服务对象
   * @return {UserService}
   */
  getService() {
    return new UserService(this);
  }
}
