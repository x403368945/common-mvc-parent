/**
 * 生成实体属性与数据库映射枚举
 * @param table {Table}
 * @returns {string}
 */
const prop = (table) => {
  const {columns, adapters} = table;
  return columns.map(column => {
    const {name} = column;
    const adapter = adapters.map(o => o.props).reduce((s, v) => Object.assign(s, v), {});
    // const defaultProp = () => `        ${this.name}(${this.dataType.value.toUpperCase()}.build(${this.notNull ? 'true, ' : ''}"${this.comment}"))`;
    // 先从 adapter 中获取 prop 适配策略，未定义策略时使用默认策略
    return (adapter[name] || adapter.default)(this);
  }).filter(Boolean).join('\n');
};
export default prop;
