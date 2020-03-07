/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const deleteByUid = (table, {auth = false, spare = false}) => {
  const {
    idType,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  const authUserId = auth ? ', userId' : '';
  return `${spareBegin}
    // 注释掉的方法只有在需要的时候解开
    @Override
    public ${TabName} deleteByUid(final ${idType} id, final String uid${authUser}) {
        return repository.deleteByUid(id, uid${authUserId});
    }
${spareEnd}`
};
export const deleteByUidOpen = table => deleteByUid(table, {auth: false, spare: false});
export const deleteByUidOpenSpare = table => deleteByUid(table, {auth: false, spare: true});

export const deleteByUidAuth = table => deleteByUid(table, {auth: true, spare: false});
export const deleteByUidAuthSpare = table => deleteByUid(table, {auth: true, spare: true});
