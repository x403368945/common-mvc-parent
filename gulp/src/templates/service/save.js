/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const save = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  return `${spareBegin}
    @Override
    public ${TabName} save(final ${TabName} obj${authUser}) {
        return repository.save(obj);
    }
${spareEnd}`
};
export const saveOpen = table => save(table, {auth: false, spare: false});
export const saveOpenSpare = table => save(table, {auth: false, spare: true});

export const saveAuth = table => save(table, {auth: true, spare: false});
export const saveAuthSpare = table => save(table, {auth: true, spare: true});
