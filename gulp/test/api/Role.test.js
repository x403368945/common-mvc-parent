/**
 * 测试：后台服务请求：角色
 * @author 谢长春 2019-8-30
 */
import RoleVO from '../../src/api/Role';
import UserTest from './User.test';
import sample from 'lodash/sample';
import AuthorityVO from '../../src/api/Authority';
import sampleSize from 'lodash/sampleSize';
import MarkDelete from '../../src/utils/entity/MarkDelete';

export default class RoleTest {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {RoleTest}
   * @return {RoleTest}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @return {RoleTest}
   */
  static of() {
    return new RoleTest();
  }

  /**
   *
   * @param func {function}
   * @return {Promise<RoleTest>}
   */
  async call(func = () => undefined) {
    func();
    return this;
  }

  /**
   * @return {Promise<RoleTest>}
   */
  async save() {
    console.log('> 新增 ----------------------------------------------------------------------------------------------------');
    const {data: tree} = (await new AuthorityVO().getService().getTree()).assertCode().assertData();
    const arrs = [
      {
        name: '随机部分权限',
        authorityTree: JSON.parse(JSON.stringify(tree).replace(/false/g,
          () => parseInt(`${Math.random()}`.slice(-1)) % 2 === 0 ? 'true' : 'false'
        ))
      },
      {
        name: '全部权限',
        authorityTree: JSON.parse(JSON.stringify(tree).replace(/false/g, 'true'))
      }
    ];
    for (let i = 0, len = arrs.length; i < len; i++) {
      (await new RoleVO(arrs[i]).getService().save()).print().assertVersion().assertData();
      (await new RoleVO(arrs[i]).getService().save()).print().assertVersion().assertData();
    }
    return this;
  }

  /**
   * @return {Promise<RoleTest>}
   */
  async update() {
    console.log('> 修改：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data: tree} = (await new AuthorityVO().getService().getTree()).assertCode().assertData();

    const {data} = (await new RoleVO().getService().pageable()).print().assertData();
    const row = Object.assign(sample(data.filter(row => ![1, 2].includes(row.id))), {
      name: '重命名角色',
      authorityTree: JSON.parse(JSON.stringify(tree).replace(/false/g,
        () => parseInt(`${Math.random()}`.slice(-1)) % 2 === 0 ? 'true' : 'false'
      ))
    });
    (await new RoleVO(row).getService().update()).print().assertVersion().assertCode();

    return this;
  }

  /**
   * @return {Promise<RoleTest>}
   */
  async markDeleteByUId() {
    console.log('> 按 id + uid 逻辑删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new RoleVO().getService().pageable()).assertData();
    const {id, uid} = sample(data.filter(row => ![1, 2].includes(row.id)));
    (await new RoleVO({
      id,
      uid
    }).getService().markDeleteByUid()).print().assertVersion().assertCode();
    return this;
  }

  /**
   * @return {Promise<RoleTest>}
   */
  async markDelete() {
    console.log('> 按 id + uid 批量逻辑删除：在查询结果集中随机选取 2 条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new RoleVO().getService().pageable()).assertData();
    const arrs = sampleSize(data.filter(row => ![1, 2].includes(row.id)), 2);
    (await new RoleVO({
      markDeleteArray: arrs.map(row => new MarkDelete(row))
    }).getService().markDelete()).print().assertVersion().assertCode();
    return this;
  }

  /**
   * @return {Promise<RoleTest>}
   */
  async findByUid() {
    console.log('> 按 id + uid 查询单条记录 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new RoleVO().getService().pageable()).assertData();
    const {id, uid} = sample(data);
    (await new RoleVO({
      id,
      uid
    }).getService().findByUid()).print().assertVersion().assertCode();
    return this;
  }

  /**
   *
   * @return {Promise<RoleTest>}
   */
  async pageable() {
    console.log('> 分页：多条件批量查询 ----------------------------------------------------------------------------------------------------');
    (await new RoleVO().getService().pageable()).print().assertVersion().assertData();
    (await new RoleVO({
      id: 1,
      deleted: 'NO'
    }).getService().pageable()).print().assertVersion().assertData();
    return this;
  }

  /**
   * @return {Promise<RoleTest>}
   */
  async options() {
    console.log('> 角色下拉列表选项 ----------------------------------------------------------------------------------------------------');
    (await new RoleVO().getService().options()).print().assertVersion().assertData();
    return this;
  }

  /**
   *
   * @return {RoleTest}
   */
  filename() {
    console.log(__filename);
    console.log('');
    return this;
  }

  /**
   *
   * @return {RoleTest}
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
    const moduleName = '角色';
    console.info(`${moduleName}：start ${'*'.repeat(200)}`);
    await Promise.resolve(RoleTest.of())
      .then(service => service.filename())
      // admin 登录
      .then(service => service.call(() => UserTest.of().loginAdminBasic()))
      // 开始
      .then(service => service.save()).then(s => s.newline())
      .then(service => service.update()).then(s => s.newline())
      .then(service => service.markDeleteByUId()).then(s => s.newline())
      .then(service => service.markDelete()).then(s => s.newline())
      .then(service => service.findByUid()).then(s => s.newline())
      .then(service => service.pageable()).then(s => s.newline())
      .then(service => service.options()).then(s => s.newline())
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
