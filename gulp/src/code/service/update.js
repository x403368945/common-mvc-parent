/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const update = (table, {auth = false, spare = false}) => {
  const {
    idType,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  const authUserId = auth ? ', userId' : '';
  return `${spareBegin}
    @Override
    public void update(final ${idType} id${authUser}, final ${TabName} obj) {
        UpdateRowsException.asserts(repository.update(id${authUserId}, obj));
    }
${spareEnd}`
};
export const updateOpen = table => update(table, {auth: false, spare: false});
export const updateOpenSpare = table => update(table, {auth: false, spare: true});

export const updateAuth = table => update(table, {auth: true, spare: false});
export const updateAuthSpare = table => update(table, {auth: true, spare: true});
