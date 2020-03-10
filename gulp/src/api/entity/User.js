import UserService from '../UserService';

/**
 * 实体：用户
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
   * @param avatar {FileInfo} 用户头像
   * @param roles {Array<number>} 角色 ID 集合
   * @param domain {string} 该属性目前只有测试时使用，常规接口不用传值，测试接口传 test
   * @param insertTime {string} 创建时间
   * @param insertUserId {number} 创建用户ID
   * @param insertUserName {string} 创建用户昵称
   * @param updateTime {string} 修改时间
   * @param updateUserId {number} 修改用户ID
   * @param updateUserName {string} 修改用户昵称
   * @param deleted {string} 逻辑删除状态，参考 {@link Bool}.*.key
   * @param roleList {Array<Role>} 新增用户时，选择的角色集合，经过验证之后，将角色 ID 保存到 roles
   * @param authorityList {Array<string>} 角色对应的权限指令集合
   * @param roleId {number} 角色 id {@link Role#id}，按角色查询用户列表时使用该字段
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
                avatar = undefined,
                roles = undefined,
                domain = undefined,
                insertTime = undefined,
                insertUserId = undefined,
                insertUserName = undefined,
                updateTime = undefined,
                updateUserId = undefined,
                updateUserName = undefined,
                deleted = undefined,
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
     * 用户头像
     * @type {FileInfo}
     */
    this.avatar = avatar;
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
     * 创建时间
     * @type {string}
     */
    this.insertTime = insertTime;
    /**
     * 创建用户ID
     * @type {number}
     */
    this.insertUserId = insertUserId;
    /**
     * 创建用户昵称
     * @type {string}
     */
    this.insertUserName = insertUserName;
    /**
     * 修改时间
     * @type {string}
     */
    this.updateTime = updateTime;
    /**
     * 修改用户ID
     * @type {number}
     */
    this.updateUserId = updateUserId;
    /**
     * 修改用户昵称
     * @type {string}
     */
    this.updateUserName = updateUserName;
    /**
     * 逻辑删除状态，参考 {@link Bool}.*.key
     * @type {string}
     */
    this.deleted = deleted;
    /**
     * 新增用户时，选择的角色集合，经过验证之后，将角色 ID 保存到 roles
     * @type {Array<Role>}
     */
    this.roleList = roleList;
    /**
     * 角色对应的权限指令集合
     * @type {Array<string>}
     */
    this.authorityList = authorityList;
    /**
     * 角色 id {@link Role#id}
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

  getAvatarUrl() {
    const {url} = this.avatar || {};
    return url || '/avatar.png'; // 当用户未上传头像时使用默认头像
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
