/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findById = (table, {auth = false, spare = false}) => {
  const {
    idType,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    @Override // <
    public Optional<${TabName}> findById(final ${idType} id) {
        return repository.findById(id);
//         return Optional.ofNullable(repository.findCacheById(id)); // 若使用缓存需要解开代码
    }
${spareEnd}`
};
export const findByIdOpen = table => findById(table, {auth: false, spare: false});
export const findByIdOpenSpare = table => findById(table, {auth: false, spare: true});

export const findByIdAuth = table => findById(table, {auth: true, spare: false});
export const findByIdAuthSpare = table => findById(table, {auth: true, spare: true});
