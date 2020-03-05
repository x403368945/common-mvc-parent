/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDeleteByUid = (table, {auth = false, spare = false}) => {
  const {
    idType
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  const authUserId = auth ? ', userId' : '';
  return `${spareBegin}
    @Override
    public void markDeleteByUid(final ${idType} id, final String uid${authUser}) {
        DeleteRowsException.asserts(repository.markDeleteByUid(id, uid${authUserId}));
    }
${spareEnd}`
};
export const markDeleteByUidOpen = table => markDeleteByUid(table, {auth: false, spare: false});
export const markDeleteByUidOpenSpare = table => markDeleteByUid(table, {auth: false, spare: true});

export const markDeleteByUidAuth = table => markDeleteByUid(table, {auth: true, spare: false});
export const markDeleteByUidAuthSpare = table => markDeleteByUid(table, {auth: true, spare: true});
