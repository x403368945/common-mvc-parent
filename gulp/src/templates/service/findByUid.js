/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findByUid = (table, {auth = false, spare = false}) => {
  const {
    idType,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    @Override // <
    public Optional<${TabName}> findByUid(final ${idType} id, final String uid) {
        return repository.findById(id).filter(row -> Objects.equals(row.getUid(), uid));
//         return Optional.ofNullable(repository.findCacheById(id)).filter(row -> Objects.equals(uid, row.getUid())); // 若使用缓存需要解开代码
    }
${spareEnd}`
};
export const findByUidOpen = table => findByUid(table, {auth: false, spare: false});
export const findByUidOpenSpare = table => findByUid(table, {auth: false, spare: true});

export const findByUidAuth = table => findByUid(table, {auth: true, spare: false});
export const findByUidAuthSpare = table => findByUid(table, {auth: true, spare: true});
