/**
 * 生成 update 代码，实体动态更新
 * @param table {Table}
 * @param excludes {Array<String>} 指定不在更新范围的字段
 * @returns {string}
 */
const update = (table, excludes = ['id', 'uid', 'deleted', 'insertTime', 'insertUserId', 'updateTime']) => {
  const {columns} = table;
  return columns.map(column => {
    const {name} = column;
    return excludes.includes(name)
      ? ''
      : `//                .then(${name}, update -> update.set(q.${name}, ${name}))`;
  }).filter(Boolean).join('\n');
};
export default update;
