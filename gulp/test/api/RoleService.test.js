/**
 * 测试：后台服务请求：角色
 * @author 谢长春 2019-8-30
 */
import sample from 'lodash/sample';
import Role from '../../src/api/entity/Role';
import UserServiceTest, {sessionUser} from './UserService.test';
import Authority from '../../src/api/entity/Authority';
import sampleSize from 'lodash/sampleSize';
import MarkDelete from '../../src/utils/entity/MarkDelete';

export default class RoleServiceTest {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {RoleServiceTest}
   * @return {RoleServiceTest}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @return {RoleServiceTest}
   */
  static of() {
    return new RoleServiceTest();
  }

  /**
   *
   * @param func {function}
   * @return {Promise<RoleServiceTest>}
   */
  async call(func = () => undefined) {
    func();
    return this;
  }

  /**
   * @return {Promise<RoleServiceTest>}
   */
  async save() {
    console.log('> 新增 ----------------------------------------------------------------------------------------------------');
    const {data: tree} = (await new Authority().getService().getTree())
      .assertCode()
      .assertData();
    const arrs = [
      {
        name: '随机部分权限',
        authorityTree: JSON.parse(JSON.stringify(tree)
          .replace(/false/, 'true') // 保证有一个选中
          .replace(/false/g, () => parseInt(`${Math.random()}`.slice(-1)) % 2 === 0 ? 'true' : 'false'))
      },
      {
        name: '全部权限',
        authorityTree: JSON.parse(JSON.stringify(tree).replace(/false/g, 'true'))
      }
    ];
    for (let i = 0, len = arrs.length; i < len; i++) {
      (await new Role(arrs[i]).getService().save())
        .print()
        .assertData();
      (await new Role(arrs[i]).getService().save())
        .print()
        .assertData();
    }
    return this;
  }

  /**
   * @return {Promise<RoleServiceTest>}
   */
  async update() {
    console.log('> 修改：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data: tree} = (await new Authority().getService().getTree()).assertCode().assertData();

    const {data} = (await new Role({
      insertUserId: sessionUser.id
    }).getService().pageable())
      .print()
      .assertData();
    const row = Object.assign(sample(data), {
      name: '重命名角色',
      authorityTree: JSON.parse(JSON.stringify(tree).replace(/false/g,
        () => parseInt(`${Math.random()}`.slice(-1)) % 2 === 0 ? 'true' : 'false'
      ))
    });
    (await new Role(row).getService().update())
      .print()
      .assertCode();

    return this;
  }

  /**
   * @return {Promise<RoleServiceTest>}
   */
  async markDeleteByUId() {
    console.log('> 按 id + uid 逻辑删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new Role({
      insertUserId: sessionUser.id
    }).getService().pageable())
      .assertData();
    const {id, uid} = sample(data);
    (await new Role({
      id,
      uid
    }).getService().markDeleteByUid())
      .print()
      .assertCode();
    return this;
  }

  /**
   * @return {Promise<RoleServiceTest>}
   */
  async markDelete() {
    console.log('> 按 id + uid 批量逻辑删除：在查询结果集中随机选取 2 条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new Role({
      insertUserId: sessionUser.id
    }).getService().pageable())
      .assertData();
    const arrs = sampleSize(data, 2);
    (await new Role({
      markDeleteArray: arrs.map(row => new MarkDelete(row))
    }).getService().markDelete())
      .print()
      .assertCode();
    return this;
  }

  /**
   * @return {Promise<RoleServiceTest>}
   */
  async findByUid() {
    console.log('> 按 id + uid 查询单条记录 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new Role().getService().pageable()).assertData();
    const {id, uid} = sample(data);
    (await new Role({
      id,
      uid
    }).getService().findByUid())
      .print()
      .assertCode();
    return this;
  }

  /**
   *
   * @return {Promise<RoleServiceTest>}
   */
  async pageable() {
    console.log('> 分页：多条件批量查询 ----------------------------------------------------------------------------------------------------');
    (await new Role().getService().pageable())
      .print()
      .assertData();
    (await new Role({
      insertUserId: sessionUser.id
    }).getService().pageable())
      .print()
      .assertData();
    (await new Role({
      id: 1,
      deleted: 'NO'
    }).getService().pageable())
      .print()
      .assertData();
    return this;
  }

  /**
   * @return {Promise<RoleServiceTest>}
   */
  async options() {
    console.log('> 角色下拉列表选项 ----------------------------------------------------------------------------------------------------');
    (await new Role().getService().options())
      .print()
      .assertData();
    return this;
  }

  /**
   *
   * @return {RoleServiceTest}
   */
  filename() {
    console.log(__filename);
    console.log('');
    return this;
  }

  /**
   *
   * @return {RoleServiceTest}
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
    await Promise.resolve(RoleServiceTest.of())
      .then(service => service.filename())
      // admin 登录
      .then(service => service.call(() => UserServiceTest.of().loginAdminBasic()))
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
