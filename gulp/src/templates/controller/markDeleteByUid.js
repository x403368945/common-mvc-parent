/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDeleteByUid = (table, {auth = false, spare = false}) => {
  const {
    comment,
    idType,
    date
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? 'final TabUser user,' : '';
  const authUserId = auth ? ', user.getId()' : '';
  return `${spareBegin}
    @PatchMapping("/{id}/{uid}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "3.逻辑删除${comment}", tags = {"${date}"})
    @ApiOperationSupport(order = 3) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<Void> markDeleteByUid(${authUser}final ${idType} id, final String uid) {
        return new Result<Void>().call(() -> service.markDeleteByUid(id, uid${authUserId}));
    }
${spareEnd}`
};
export const markDeleteByUidOpen = table => markDeleteByUid(table, {auth: false, spare: false});
export const markDeleteByUidOpenSpare = table => markDeleteByUid(table, {auth: false, spare: true});

export const markDeleteByUidAuth = table => markDeleteByUid(table, {auth: true, spare: false});
export const markDeleteByUidAuthSpare = table => markDeleteByUid(table, {auth: true, spare: true});
