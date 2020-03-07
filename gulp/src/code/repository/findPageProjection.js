/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findPageProjection = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    @Override // <
    default <T extends ${TabName}> QueryResults<T> findPageProjection(final ${TabName} condition, final Pager pager, final Class<T> clazz) {
        return findPageProjection(condition, pager, clazz, ${TabName}.allColumnAppends());
    }
${spareEnd}`
};
export const findPageProjectionOpen = table => findPageProjection(table, {auth: false, spare: false});
export const findPageProjectionOpenSpare = table => findPageProjection(table, {auth: false, spare: true});

export const findPageProjectionAuth = table => findPageProjection(table, {auth: true, spare: false});
export const findPageProjectionAuthSpare = table => findPageProjection(table, {auth: true, spare: true});
