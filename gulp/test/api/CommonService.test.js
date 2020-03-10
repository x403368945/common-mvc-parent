/**
 * 测试：后台服务请求：通用模块
 * @author 谢长春 2020-03-10
 */
import fs from 'fs'
import UserServiceTest from './UserService.test';
import CommonService from '../../src/api/CommonService';

export default class CommonServiceTest {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {CommonServiceTest}
   * @return {CommonServiceTest}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @return {CommonServiceTest}
   */
  static of() {
    return new CommonServiceTest();
  }

  /**
   *
   * @param func {function}
   * @return {Promise<CommonServiceTest>}
   */
  async call(func = () => undefined) {
    func();
    return this;
  }

  /**
   * @return {Promise<CommonServiceTest>}
   */
  async getEnumItems() {
    console.log('> 获取枚举所有选项 ----------------------------------------------------------------------------------------------------');
    (await new CommonService().getEnumItems('com.ccx.demo.business.user.entity.TabUser$OrderBy')).print().assertData();
    (await new CommonService().getEnumItems('com.ccx.demo.enums.Bool')).print().assertData();
    (await new CommonService().getEnumItems('com.support.mvc.enums.Code')).print().assertData();
    return this;
  }

  /**
   * @return {Promise<CommonServiceTest>}
   */
  async uploadTemp() {
    console.log('> 上传到临时目录，单个上传 ----------------------------------------------------------------------------------------------------');
    (await new CommonService().uploadTemp(fs.createReadStream('./favicon.ico'))).print().assertData();
    return this;
  }

  /**
   * @return {Promise<CommonServiceTest>}
   */
  async uploadTemps() {
    console.log('> 上传到临时目录，批量上传 ----------------------------------------------------------------------------------------------------');
    (await new CommonService().uploadTemps([
      fs.createReadStream('./favicon.ico'),
      fs.createReadStream('./package.json')
    ])).print().assertData();
    return this;
  }

  /**
   * @return {Promise<CommonServiceTest>}
   */
  async uploadUser() {
    console.log('> 上传到用户目录，单个上传 ----------------------------------------------------------------------------------------------------');
    (await new CommonService().uploadUser(fs.createReadStream('./favicon.ico'))).print().assertData();
    return this;
  }

  /**
   * @return {Promise<CommonServiceTest>}
   */
  async uploadUsers() {
    console.log('> 上传到用户目录，批量上传 ----------------------------------------------------------------------------------------------------');
    (await new CommonService().uploadUsers([
      fs.createReadStream('./favicon.ico'),
      fs.createReadStream('./favicon.ico')
    ])).print().assertData();
    return this;
  }

  /**
   *
   * @return {CommonServiceTest}
   */
  filename() {
    console.log(__filename);
    console.log('');
    return this;
  }

  /**
   *
   * @return {CommonServiceTest}
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
    const moduleName = '通用模块';
    console.info(`${moduleName}：start ${'*'.repeat(200)}`);
    await Promise.resolve(CommonServiceTest.of())
      .then(service => service.filename())
      // admin 登录
      .then(service => service.call(() => UserServiceTest.of().loginAdminBasic()))
      // 开始
      .then(service => service.getEnumItems()).then(s => s.newline())
      .then(service => service.uploadTemp()).then(s => s.newline())
      .then(service => service.uploadTemps()).then(s => s.newline())
      .then(service => service.uploadUser()).then(s => s.newline())
      .then(service => service.uploadUsers()).then(s => s.newline())
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
