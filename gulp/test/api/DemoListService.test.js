/**
 * 测试：后台服务请求：实体表操作
 * @author 谢长春 2019-7-28
 */
import random from 'lodash/random';
import sample from 'lodash/sample';
import sampleSize from 'lodash/sampleSize';
import DemoList from '../../src/api/entity/DemoList';
import UserServiceTest from './UserService.test';
import {DemoStatus} from '../../src/api/Enums';
import OrderBy from '../../src/utils/entity/OrderBy';
import {DateRange, NumRange} from '../../src/utils/entity/Range';

export default class DemoListServiceTest {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {DemoListServiceTest}
   * @return {DemoListServiceTest}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @return {DemoListServiceTest}
   */
  static of() {
    return new DemoListServiceTest();
  }

  /**
   *
   * @param func {function}
   * @return {Promise<DemoListServiceTest>}
   */
  async call(func = () => undefined) {
    func();
    return this;
  }

  /**
   * @return {Promise<DemoListServiceTest>}
   */
  async save() {
    console.log('> 新增 ----------------------------------------------------------------------------------------------------');
    const arrs = [
      {name: `name-${random(10000)}`},
      {name: `name-${random(10000)}`},
      {name: `name-${random(10000)}`},
      {name: `name-${random(10000)}`},
      {name: `name-${random(10000)}`},
      {
        name: `name-${random(10000)}`,
        content: `content-${random(1000000)}`
      },
      {
        name: `name-${random(10000)}`,
        content: `content-${random(1000000)}`,
        amount: random(10000.9)
      },
      {
        name: `name-${random(10000)}`,
        content: `content-${random(1000000)}`,
        amount: random(10000.9),
        status: sample(DemoStatus.options()).value
      }
    ];
    for (let i = 0, len = arrs.length; i < len; i++) {
      (await new DemoList(arrs[i]).getService().save()).print().assertData();
    }
    return this;
  }

  /**
   * @return {Promise<DemoListServiceTest>}
   */
  async update() {
    console.log('> 修改：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoList().getService().pageable()).print().assertData();
    const row = Object.assign(sample(data), {amount: random(10000.9).toFixed(2)});
    (await new DemoList(row).getService().update()).print().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListServiceTest>}
   */
  async deleteById() {
    console.log('> 按 id 删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoList().getService().pageable()).assertData();
    const {id} = sample(data);
    (await new DemoList({id}).getService().deleteById()).print().assertData();
    return this;
  }

  /**
   * @return {Promise<DemoListServiceTest>}
   */
  async deleteByUid() {
    console.log('> 按 id + uid 删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoList().getService().pageable()).assertData();
    const {id, uid} = sample(data);
    (await new DemoList({
      id,
      uid
    }).getService().deleteByUid()).print().assertData();
    return this;
  }

  /**
   * @return {Promise<DemoListServiceTest>}
   */
  async markDeleteById() {
    console.log('> 按 id 逻辑删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoList().getService().pageable()).assertData();
    const {id} = sample(data);
    (await new DemoList({id}).getService().markDeleteById()).print().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListServiceTest>}
   */
  async markDeleteByUId() {
    console.log('> 按 id + uid 逻辑删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoList().getService().pageable()).assertData();
    const {id, uid} = sample(data);
    (await new DemoList({
      id,
      uid
    }).getService().markDeleteByUid()).print().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListServiceTest>}
   */
  async markDelete() {
    console.log('> 按 id + uid 批量逻辑删除：在查询结果集中随机选取 2 条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoList().getService().pageable()).assertData();
    const uids = sampleSize(data).map(({id, uid}) => ({
      id,
      uid
    }), 2);
    (await new DemoList({uids}).getService().markDelete()).print().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListServiceTest>}
   */
  async findByIdTimestamp() {
    console.log('> 按 id + 时间戳 查询单条记录 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoList().getService().pageable()).assertData();
    const {id, timestamp} = sample(data);
    (await new DemoList({
      id,
      timestamp
    }).getService().findByIdTimestamp()).print().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListServiceTest>}
   */
  async findByUidTimestamp() {
    console.log('> 按 id + uid + 时间戳 查询单条记录 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoList().getService().pageable()).assertData();
    const {id, uid, timestamp} = sample(data);
    (await new DemoList({
      id,
      uid,
      timestamp
    }).getService().findByUidTimestamp()).print().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListServiceTest>}
   */
  async search() {
    console.log('> 多条件批量查询，不分页 ----------------------------------------------------------------------------------------------------');
    const insertTimeRange = new DateRange(new Date().addMonth(-1).formatDate(), new Date().formatDate());
    (await new DemoList({insertTimeRange}).getService().search()).print().assertData();
    return this;
  }

  /**
   *
   * @return {Promise<DemoListServiceTest>}
   */
  async pageable() {
    console.log('> 分页：多条件批量查询 ----------------------------------------------------------------------------------------------------');
    const insertTimeRange = new DateRange(new Date().addMonth(-1).formatDate(), new Date().formatDate());
    const amountRange = new NumRange(0, 9999);
    const sorts = [OrderBy.desc('updateTime')];
    (await new DemoList().getService().pageable()).print().assertData();
    (await new DemoList({sorts}).getService().pageable()).print().assertData();
    (await new DemoList({
      amountRange,
      sorts
    }).getService().pageable()).print().assertCode();
    (await new DemoList({
      insertTimeRange,
      amountRange,
      sorts
    }).getService().pageable()).print().assertCode();
    return this;
  }

  /**
   *
   * @return {DemoListServiceTest}
   */
  filename() {
    console.log(__filename);
    console.log('');
    return this;
  }

  /**
   *
   * @return {DemoListServiceTest}
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
    const moduleName = '实体表操作';
    console.info(`${moduleName}：start ${'*'.repeat(200)}`);
    await Promise.resolve(DemoListServiceTest.of())
      .then(service => service.filename())
      // admin 登录
      .then(service => service.call(() => UserServiceTest.of().loginAdminBasic()))
      // 开始
      .then(service => service.save()).then(s => s.newline())
      .then(service => service.update()).then(s => s.newline())
      .then(service => service.deleteById()).then(s => s.newline())
      .then(service => service.deleteByUid()).then(s => s.newline())
      .then(service => service.markDeleteById()).then(s => s.newline())
      .then(service => service.markDeleteByUId()).then(s => s.newline())
      .then(service => service.markDelete()).then(s => s.newline())
      .then(service => service.findByIdTimestamp()).then(s => s.newline())
      .then(service => service.findByUidTimestamp()).then(s => s.newline())
      .then(service => service.search()).then(s => s.newline())
      .then(service => service.pageable()).then(s => s.newline())
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
