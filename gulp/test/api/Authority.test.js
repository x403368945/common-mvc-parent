/**
 * 测试：后台服务请求：权限指令
 * @author 谢长春 2019-8-30
 */
import {AuthorityService} from '../../src/api/Authority';
import UserTest from './User.test';

export default class AuthorityTest {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {AuthorityTest}
   * @return {AuthorityTest}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @return {AuthorityTest}
   */
  static of() {
    return new AuthorityTest();
  }

  /**
   *
   * @param func {function}
   * @return {Promise<AuthorityTest>}
   */
  async call(func = () => undefined) {
    func();
    return this;
  }

  /**
   * @return {Promise<AuthorityTest>}
   */
  async getTree() {
    console.log('> 查询权限指令树 ----------------------------------------------------------------------------------------------------');
    (await new AuthorityService().getTree()).print().assertVersion().assertData();
    return this;
  }

  /**
   * @return {Promise<AuthorityTest>}
   */
  async getList() {
    console.log('> 查询权限指令列表 ----------------------------------------------------------------------------------------------------');
    (await new AuthorityService().getList()).print().assertVersion().assertData();
    return this;
  }

  /**
   *
   * @return {AuthorityTest}
   */
  filename() {
    console.log(__filename);
    return this;
  }

  /**
   *
   * @return {AuthorityTest}
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
    await Promise.resolve(AuthorityTest.of())
      .then(service => service.filename()).then(s => s.newline())
      // admin 登录
      .then(service => service.call(() => UserTest.of().loginAdminBasic()))
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
