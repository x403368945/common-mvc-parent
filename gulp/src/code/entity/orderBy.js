/**
 * 生成 order by 代码，实体排序枚举
 * @param table {Table}
 * @returns {string}
 */
const orderBy = table => {
  const {columns, names: {tabName}} = table;
  return columns.map(column => {
    const {name} = column;
    return `        ${name}(${tabName}.${name})`;
  }).filter(Boolean).join(',\n');
};
export default orderBy;
