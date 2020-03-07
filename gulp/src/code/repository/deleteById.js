/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const deleteById = (table, {auth = false, spare = false}) => {
  const {
    idType,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  const authUserId = auth ? '.insertUserId(userId)' : '';
  return `${spareBegin}
//     @CacheEvict(cacheNames = I${TabName}Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
    @Override
    default ${TabName} deleteById(final ${idType} id${authUser}) {
        return Optional
                .ofNullable(jpaQueryFactory.<JPAQueryFactory>get()
                        .selectFrom(q)
                        .where(q.id.eq(id))
                        .fetchOne()
                )
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        ${TabName}.builder().id(id)${authUserId}.build().json())
                ));
    }
${spareEnd}`
};
export const deleteByIdOpen = table => deleteById(table, {auth: false, spare: false});
export const deleteByIdOpenSpare = table => deleteById(table, {auth: false, spare: true});

export const deleteByIdAuth = table => deleteById(table, {auth: true, spare: false});
export const deleteByIdAuthSpare = table => deleteById(table, {auth: true, spare: true});
