/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findListExpression = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    @Override // <
    default List<${TabName}> findList(final ${TabName} condition) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }
${spareEnd}`
};
export const findListExpressionOpen = table => findListExpression(table, {auth: false, spare: false});
export const findListExpressionOpenSpare = table => findListExpression(table, {auth: false, spare: true});

export const findListExpressionAuth = table => findListExpression(table, {auth: true, spare: false});
export const findListExpressionAuthSpare = table => findListExpression(table, {auth: true, spare: true});
