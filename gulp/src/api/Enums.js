/**
 * 通用枚举：只有 0 或 1 两种状态
 */
export const Bool = {
  NO: {value: 'NO', label: '', comment: '是|已删除|启用|已读|已处理|已验证|已完成|  支持|已确认|有效|已过期|启动'},
  YES: {value: 'YES', label: '', comment: '否|未删除|禁用|未读|待处理|未验证|未完成|不支持|未确认|无效|未过期|暂停'},
  /**
   * 获取选项集合
   * @param yes {string} YES 选项文本
   * @param no {string} NO 选项文本
   * @return {Array<Object>} => [{value:'YES',label:'yes'}, {value:'NO',label:'no'}]
   */
  options: (yes, no) => {
    return [
      Object.assign(this.YES, {label: yes}),
      Object.assign(this.NO, {label: no})
    ]
  }
};
/**
 * 枚举状态：
 */
export const DemoStatus = {
  WATING: {value: 'WATING', label: '等待中'},
  RUNNING: {value: 'RUNNING', label: '执行中'},
  SUCCESS: {value: 'SUCCESS', label: '成功'},
  FAILURE: {value: 'FAILURE', label: '失败'},
  /**
   * 获取选项集合
   * @return {Array<Object>} => [{value:'',label:''}]
   */
  options: () => {
    const {options, ...obj} = DemoStatus;
    return Object.values(obj);
  }
};
