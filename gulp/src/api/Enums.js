/**
 * 枚举选项通用对象
 */
export class Item {
  /**
   *
   * @param key {string} 后台接口需要的值
   * @param comment {string} 前端显示值
   */
  constructor(key, comment) {
    /**
     * 后台接口需要的值
     * @type {string}
     */
    this.key = key;
    /**
     * 前端显示值
     * @type {string}
     */
    this.comment = comment;
  }
}

/**
 * 通用枚举：只有 0 或 1 两种状态
 */
export const Bool = {
  NO: new Item('NO', '是|已删除|启用|已读|已处理|已验证|已完成|  支持|已确认|有效|已过期|启动'),
  YES: new Item('YES', '否|未删除|禁用|未读|待处理|未验证|未完成|不支持|未确认|无效|未过期|暂停'),
  /**
   * 获取选项集合
   * @param yes {string} YES 选项文本
   * @param no {string} NO 选项文本
   * @return {Array<Item>} => [{key:'YES',comment:'yes'}, {key:'NO',comment:'no'}]
   */
  options: (yes, no) => {
    return [
      new Item(this.YES.key, yes),
      new Item(this.NO.key, no)
    ]
  }
};
/**
 * 枚举状态：
 * @type {{SUCCESS: Item, RUNNING: Item, WATING: Item, options: (function(): Item[]), FAILURE: Item}}
 */
export const DemoStatus = {
  WATING: new Item('WATING', '等待中'),
  RUNNING: new Item('RUNNING', '执行中'),
  SUCCESS: new Item('SUCCESS', '成功'),
  FAILURE: new Item('FAILURE', '失败'),
  /**
   * 获取选项集合
   * @return {Array<Item>}
   */
  options: () => {
    const {options, ...obj} = DemoStatus;
    return Object.values(obj);
  }
};
