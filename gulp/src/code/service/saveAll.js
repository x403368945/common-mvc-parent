/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const saveAll = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  return `${spareBegin}
    @Override // <
    public List<${TabName}> saveAll(final List<${TabName}> list${authUser}) {
        return repository.saveAll(list);
    }
${spareEnd}`
};
export const saveAllOpen = table => saveAll(table, {auth: false, spare: false});
export const saveAllOpenSpare = table => saveAll(table, {auth: false, spare: true});

export const saveAllAuth = table => saveAll(table, {auth: true, spare: false});
export const saveAllAuthSpare = table => saveAll(table, {auth: true, spare: true});
