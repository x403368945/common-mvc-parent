/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findListProjection = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    @Override // <
    default <T extends ${TabName}> List<T> findListProjection(final ${TabName} condition, final Class<T> clazz) {
        return findListProjection(condition, clazz, ${TabName}.allColumnAppends());
    }
${spareEnd}`
};
export const findListProjectionOpen = table => findListProjection(table, {auth: false, spare: false});
export const findListProjectionOpenSpare = table => findListProjection(table, {auth: false, spare: true});

export const findListProjectionAuth = table => findListProjection(table, {auth: true, spare: false});
export const findListProjectionAuthSpare = table => findListProjection(table, {auth: true, spare: true});
