/**
 * 测试：后台服务请求：实体表操作
 * @author 谢长春 2019-7-28
 */
import DemoListVO from '../../src/api/DemoList';
import UserTest from './User.test';
import random from 'lodash/random';
import sample from 'lodash/sample';
import sampleSize from 'lodash/sampleSize';
import {DemoStatus} from '../../src/api/Enums';
import OrderBy from '../../src/utils/entity/OrderBy';
import {DateRange, NumRange} from '../../src/utils/entity/Range';

export default class DemoListTest {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {DemoListTest}
   * @return {DemoListTest}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @return {DemoListTest}
   */
  static of() {
    return new DemoListTest();
  }

  /**
   *
   * @param func {function}
   * @return {Promise<DemoListTest>}
   */
  async call(func = () => undefined) {
    func();
    return this;
  }

  /**
   * @return {Promise<DemoListTest>}
   */
  async save() {
    console.log('> 新增 ----------------------------------------------------------------------------------------------------');
    const arrs = [
      {name: `name-${random(10000)}`},
      {name: `name-${random(10000)}`},
      {name: `name-${random(10000)}`},
      {name: `name-${random(10000)}`},
      {name: `name-${random(10000)}`},
      {name: `name-${random(10000)}`, content: `content-${random(1000000)}`},
      {name: `name-${random(10000)}`, content: `content-${random(1000000)}`, amount: random(10000.9)},
      {
        name: `name-${random(10000)}`,
        content: `content-${random(1000000)}`,
        amount: random(10000.9),
        status: sample(DemoStatus.options()).value
      }
    ];
    for (let i = 0, len = arrs.length; i < len; i++) {
      (await new DemoListVO(arrs[i]).getService().save()).print().assertVersion().assertData();
    }
    return this;
  }

  /**
   * @return {Promise<DemoListTest>}
   */
  async update() {
    console.log('> 修改：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoListVO().getService().pageable()).print().assertData();
    const row = Object.assign(sample(data), {amount: random(10000.9).toFixed(2)});
    (await new DemoListVO(row).getService().update()).print().assertVersion().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListTest>}
   */
  async deleteById() {
    console.log('> 按 id 删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoListVO().getService().pageable()).assertData();
    const {id} = sample(data);
    (await new DemoListVO({id}).getService().deleteById()).print().assertVersion().assertData();
    return this;
  }

  /**
   * @return {Promise<DemoListTest>}
   */
  async deleteByUid() {
    console.log('> 按 id + uid 删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoListVO().getService().pageable()).assertData();
    const {id, uid} = sample(data);
    (await new DemoListVO({id, uid}).getService().deleteByUid()).print().assertVersion().assertData();
    return this;
  }

  /**
   * @return {Promise<DemoListTest>}
   */
  async markDeleteById() {
    console.log('> 按 id 逻辑删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoListVO().getService().pageable()).assertData();
    const {id} = sample(data);
    (await new DemoListVO({id}).getService().markDeleteById()).print().assertVersion().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListTest>}
   */
  async markDeleteByUId() {
    console.log('> 按 id + uid 逻辑删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoListVO().getService().pageable()).assertData();
    const {id, uid} = sample(data);
    (await new DemoListVO({id, uid}).getService().markDeleteByUid()).print().assertVersion().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListTest>}
   */
  async markDelete() {
    console.log('> 按 id + uid 批量逻辑删除：在查询结果集中随机选取 2 条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoListVO().getService().pageable()).assertData();
    const uids = sampleSize(data).map(({id, uid}) => ({id, uid}), 2);
    (await new DemoListVO({uids}).getService().markDelete()).print().assertVersion().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListTest>}
   */
  async findByIdTimestamp() {
    console.log('> 按 id + 时间戳 查询单条记录 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoListVO().getService().pageable()).assertData();
    const {id, timestamp} = sample(data);
    (await new DemoListVO({id, timestamp}).getService().findByIdTimestamp()).print().assertVersion().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListTest>}
   */
  async findByUidTimestamp() {
    console.log('> 按 id + uid + 时间戳 查询单条记录 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new DemoListVO().getService().pageable()).assertData();
    const {id, uid, timestamp} = sample(data);
    (await new DemoListVO({id, uid, timestamp}).getService().findByUidTimestamp()).print().assertVersion().assertCode();
    return this;
  }

  /**
   * @return {Promise<DemoListTest>}
   */
  async search() {
    console.log('> 多条件批量查询，不分页 ----------------------------------------------------------------------------------------------------');
    const createTimeRange = new DateRange(new Date().addMonth(-1).formatDate(), new Date().formatDate());
    (await new DemoListVO({createTimeRange}).getService().search()).print().assertVersion().assertData();
    return this;
  }

  /**
   *
   * @return {Promise<DemoListTest>}
   */
  async pageable() {
    console.log('> 分页：多条件批量查询 ----------------------------------------------------------------------------------------------------');
    const createTimeRange = new DateRange(new Date().addMonth(-1).formatDate(), new Date().formatDate());
    const amountRange = new NumRange(0, 9999);
    const sorts = [OrderBy.desc('modifyTime')];
    (await new DemoListVO().getService().pageable()).print().assertVersion().assertData();
    (await new DemoListVO({sorts}).getService().pageable()).print().assertVersion().assertData();
    (await new DemoListVO({amountRange, sorts}).getService().pageable()).print().assertVersion().assertData();
    (await new DemoListVO({
      createTimeRange,
      amountRange,
      sorts
    }).getService().pageable()).print().assertVersion().assertData();
    return this;
  }

  /**
   *
   * @return {Promise<DemoListTest>}
   */
  filename() {
    console.log(__filename);
    return this;
  }

  /**
   *
   * @return {Promise<DemoListTest>}
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
    await Promise.resolve(DemoListTest.of())
      .then(service => service.filename()).then(s => s.newline())
      // admin 登录
      .then(service => service.call(() => UserTest.of().loginAdminBasic()))
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
