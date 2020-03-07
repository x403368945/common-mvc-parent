/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDeleteById = (table, {auth = false, spare = false}) => {
  const {
    idType,
    // names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  const authUserId = auth ? ', userId' : '';
  return `${spareBegin}
    // 注释掉的方法只有在需要的时候解开
    @Override
    public void markDeleteById(final ${idType} id${authUser}) {
        DeleteRowsException.asserts(repository.markDeleteById(id${authUserId}));
    }
${spareEnd}`
};
export const markDeleteByIdOpen = table => markDeleteById(table, {auth: false, spare: false});
export const markDeleteByIdOpenSpare = table => markDeleteById(table, {auth: false, spare: true});

export const markDeleteByIdAuth = table => markDeleteById(table, {auth: true, spare: false});
export const markDeleteByIdAuthSpare = table => markDeleteById(table, {auth: true, spare: true});
