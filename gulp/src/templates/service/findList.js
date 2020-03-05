/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findList = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    // 非必要情况下不要开放列表查询方法，因为没有分页控制，容易内存溢出。大批量查询数据应该使用分页查询 <
    @Override
    public List<${TabName}> findList(final ${TabName} condition) {
        return repository.findList(condition);
    }
${spareEnd}`
};
export const findListOpen = table => findList(table, {auth: false, spare: false});
export const findListOpenSpare = table => findList(table, {auth: false, spare: true});

export const findListAuth = table => findList(table, {auth: true, spare: false});
export const findListAuthSpare = table => findList(table, {auth: true, spare: true});
