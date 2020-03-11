/**
 * 生成 where 条件代码，实体动态查询
 * @param table {Table}
 * @returns {string}
 */
const where = table => {
  const {columns} = table;
  return columns
    .map(column => {
      const {name, dataType, length} = column;
      switch (dataType.mysql) {
        case 'CHAR':
          return `//                .and(${name}, () -> ${name}.endsWith("%") || ${name}.startsWith("%") ? q.${name}.like(${name}) : q.${name}.eq(${name}))`;
        case 'VARCHAR':
          if (name === 'uid') break;
          return length < 100
            ? `//                .and(${name}, () -> ${name}.endsWith("%") || ${name}.startsWith("%") ? q.${name}.like(${name}) : q.${name}.eq(${name}))`
            : `//                .and(${name}, () -> ${name}.endsWith("%") || ${name}.startsWith("%") ? q.${name}.like(${name}) : q.${name}.startsWith(${name}))`;
        case 'TEXT':
        case 'MEDIUMTEXT':
        case 'LONGTEXT':
          return `//                .and(${name}, () -> ${name}.endsWith("%") || ${name}.startsWith("%") ? q.${name}.like(${name}) : q.${name}.contains(${name}))`;
        case 'DECIMAL':
        case 'DOUBLE':
        case 'FLOAT':
          return `//                .and(${name}Range, () -> q.${name}.between(${name}Range.getMin(), ${name}Range.getMax()))`;
        case 'DATE':
        case 'DATETIME':
        case 'TIMESTAMP':
          return `//                .and(${name}Range, () -> q.${name}.between(${name}Range.rebuild().getBegin(), ${name}Range.getEnd()))`;
        case 'JSON':
          return `//                .and(${name}, () -> Expressions.booleanTemplate("JSON_CONTAINS({0},{1})>0", q.${name}, JSON.toJSONString(${name})))`;
      }
      return `//                .and(${name}, () -> q.${name}.eq(${name}))`;
    })
    .filter(Boolean).join('\n');
};
export default where;
