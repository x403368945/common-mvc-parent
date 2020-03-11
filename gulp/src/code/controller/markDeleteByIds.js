/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDeleteByIds = (table, {auth = false, spare = false}) => {
  const {
    comment,
    idType,
    date
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? 'final TabUser user, ' : '';
  const authUserId = auth ? ', user.getId()' : '';
  return `${spareBegin}
    @PatchMapping
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "4.批量逻辑删除${comment}", tags = {"${date}"})
    @ApiOperationSupport(order = 4) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<Void> markDeleteByIds(${authUser}final Set<${idType}> body) {
        return new Result<Void>().call(() -> service.markDeleteByIds(body${authUserId}));
    }
${spareEnd}`
};
export const markDeleteByIdsOpen = table => markDeleteByIds(table, {auth: false, spare: false});
export const markDeleteByIdsOpenSpare = table => markDeleteByIds(table, {auth: false, spare: true});

export const markDeleteByIdsAuth = table => markDeleteByIds(table, {auth: true, spare: false});
export const markDeleteByIdsAuthSpare = table => markDeleteByIds(table, {auth: true, spare: true});
