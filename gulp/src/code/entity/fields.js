/**
 * 生成实体类属性字段定义代码
 * @param table {Table}
 * @returns {string}
 */
const fields = table => {
  const {columns, adapters} = table;
  return columns.map(column => {
    const {name, comment} = column;
    const adapter = adapters.map(o => o.fields).reduce((s, v) => Object.assign(s, v), {});
    return `    /**\n     * ${comment}\n     */\n`.concat((adapter[name] || adapter.default)(column));
  }).filter(Boolean).join('\n');
};
export default fields;
