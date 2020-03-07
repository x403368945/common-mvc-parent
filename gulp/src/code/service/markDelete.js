/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDelete = (table, {auth = false, spare = false}) => {
  const {
    idType
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  const authUserId = auth ? ', userId' : '';
  return `${spareBegin}
    @Override // <
    public void markDelete(final List<MarkDelete> list${authUser}) {
        DeleteRowsException.asserts(repository.markDelete(list${authUserId}), list.size());
        //clearKeys(list.stream().map(MarkDelete::get${idType}Id).collect(Collectors.toSet())); // 若使用缓存需要解开代码
    }
${spareEnd}`
};
export const markDeleteOpen = table => markDelete(table, {auth: false, spare: false});
export const markDeleteOpenSpare = table => markDelete(table, {auth: false, spare: true});

export const markDeleteAuth = table => markDelete(table, {auth: true, spare: false});
export const markDeleteAuthSpare = table => markDelete(table, {auth: true, spare: true});
