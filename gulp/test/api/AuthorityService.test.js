/**
 * 测试：后台服务请求：权限指令
 * @author 谢长春 2019-8-30
 */
import UserServiceTest from './UserService.test';
import AuthorityService from '../../src/api/AuthorityService';

export default class AuthorityServiceTest {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {AuthorityServiceTest}
   * @return {AuthorityServiceTest}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @return {AuthorityServiceTest}
   */
  static of() {
    return new AuthorityServiceTest();
  }

  /**
   *
   * @param func {function}
   * @return {Promise<AuthorityServiceTest>}
   */
  async call(func = () => undefined) {
    func();
    return this;
  }

  /**
   * @return {Promise<AuthorityServiceTest>}
   */
  async getTree() {
    console.log('> 查询权限指令树 ----------------------------------------------------------------------------------------------------');
    (await new AuthorityService().getTree()).print().assertData();
    return this;
  }

  /**
   * @return {Promise<AuthorityServiceTest>}
   */
  async getList() {
    console.log('> 查询权限指令列表 ----------------------------------------------------------------------------------------------------');
    (await new AuthorityService().getList()).print().assertData();
    return this;
  }

  /**
   *
   * @return {AuthorityServiceTest}
   */
  filename() {
    console.log(__filename);
    console.log('');
    return this;
  }

  /**
   *
   * @return {AuthorityServiceTest}
   */
  newline() {
    console.log('');
    return this;
  }

  /**
   * 测试全部
   * @return {Promise<void>}
   */
  async testAll() {
    const moduleName = '权限指令';
    console.info(`${moduleName}：start ${'*'.repeat(200)}`);
    await Promise.resolve(AuthorityServiceTest.of())
      .then(service => service.filename())
      // admin 登录
      .then(service => service.call(() => UserServiceTest.of().loginAdminBasic()))
      // 开始
      .then(service => service.getTree()).then(s => s.newline())
      .then(service => service.getList()).then(s => s.newline())
      // 结束
      .then(() => console.info(`${moduleName}：end ${'*'.repeat(200)}\n\n\n\n\n`))
      .catch((e) => {
        console.info(`${moduleName}：异常：${e.message}`);
        console.error(e)
      })
    ;
    return null;
  }
}
