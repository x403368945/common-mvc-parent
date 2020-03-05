/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDeleteById = (table, {auth = false, spare = false}) => {
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
    // 优先使用 markDeleteByUid 方法，可以阻止平行越权。 只有在实体没有 uid 的情况才能将该方法开放给前端<
    @PatchMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_delete')")
    @ApiOperation(value = "3.逻辑删除${comment}", tags = {"${date}"})
    @ApiOperationSupport(order = 3) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<Void> markDeleteById(${authUser}final ${idType} id) {
        return new Result<Void>().call(() -> service.markDeleteById(id${authUserId}));
    }
${spareEnd}`
};
export const markDeleteByIdOpen = table => markDeleteById(table, {auth: false, spare: false});
export const markDeleteByIdOpenSpare = table => markDeleteById(table, {auth: false, spare: true});

export const markDeleteByIdAuth = table => markDeleteById(table, {auth: true, spare: false});
export const markDeleteByIdAuthSpare = table => markDeleteById(table, {auth: true, spare: true});
