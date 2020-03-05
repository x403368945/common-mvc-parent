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
  const authUserId = auth ? '.insertUserId(userId)' : '';
  return `${spareBegin}
//     @CacheEvict(cacheNames = I${TabName}Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
    @Override
    default ${TabName} deleteByUid(final ${idType} id, final String uid${authUser}) {
        // userId 为可选校验，一般业务场景，能获取到 UUID 已经表示已经加强校验了
        return Optional
                .ofNullable(jpaQueryFactory.<JPAQueryFactory>get()
                        .selectFrom(q)
                        .where(q.id.eq(id).and(q.uid.eq(uid)))
                        .fetchOne()
                )
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        ${TabName}.builder().id(id).uid(uid)${authUserId}.build().json())
                ));
    }
${spareEnd}`
};
export const deleteByUidOpen = table => deleteByUid(table, {auth: false, spare: false});
export const deleteByUidOpenSpare = table => deleteByUid(table, {auth: false, spare: true});

export const deleteByUidAuth = table => deleteByUid(table, {auth: true, spare: false});
export const deleteByUidAuthSpare = table => deleteByUid(table, {auth: true, spare: true});
