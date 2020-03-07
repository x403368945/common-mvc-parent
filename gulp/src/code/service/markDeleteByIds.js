/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDeleteByIds = (table, {auth = false, spare = false}) => {
  const {
    idType
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  const authUserId = auth ? ', userId' : '';
  return `${spareBegin}
    // 注释掉的方法只有在需要的时候解开 <
    @Override
    public void markDeleteByIds(final List<${idType}> ids${authUser}) {
        DeleteRowsException.asserts(repository.markDeleteByIds(ids${authUserId}), ids.size());
        //clearKeys(ids); // 若使用缓存需要解开代码
    }
${spareEnd}`
};
export const markDeleteByIdsOpen = table => markDeleteByIds(table, {auth: false, spare: false});
export const markDeleteByIdsOpenSpare = table => markDeleteByIds(table, {auth: false, spare: true});

export const markDeleteByIdsAuth = table => markDeleteByIds(table, {auth: true, spare: false});
export const markDeleteByIdsAuthSpare = table => markDeleteByIds(table, {auth: true, spare: true});
