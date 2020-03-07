/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findListProjectionExpression = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    @Override // <
    default <T extends ${TabName}> List<T> findListProjection(final ${TabName} condition, final Class<T> clazz, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }
${spareEnd}`
};
export const findListProjectionExpressionOpen = table => findListProjectionExpression(table, {auth: false, spare: false});
export const findListProjectionExpressionOpenSpare = table => findListProjectionExpression(table, {auth: false, spare: true});

export const findListProjectionExpressionAuth = table => findListProjectionExpression(table, {auth: true, spare: false});
export const findListProjectionExpressionAuthSpare = table => findListProjectionExpression(table, {auth: true, spare: true});
