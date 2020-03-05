/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const update = (table, {auth = false, spare = false}) => {
  const {
    comment,
    idType,
    date,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? 'final TabUser user,' : '';
  const authUserId = auth ? ', user.getId()' : '';
  return `${spareBegin}
    @PutMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_update')") // <
    @ApiOperation(value = "2.修改${comment}", tags = {"${date}"})
    @ApiImplicitParam(name = "body", dataType = "${TabName}", dataTypeClass = ${TabName}.class, required = true)
    @ApiOperationSupport(
            order = 2,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
            })
    @ResponseBody
    @Override
    public Result<Void> update(${authUser}final ${idType} id, final String body) {
        return new Result<Void>().call(() -> service.update(id${authUserId}, JSON.parseObject(body, ${TabName}.class)));
    }
${spareEnd}`
};
export const updateOpen = table => update(table, {auth: false, spare: false});
export const updateOpenSpare = table => update(table, {auth: false, spare: true});

export const updateAuth = table => update(table, {auth: true, spare: false});
export const updateAuthSpare = table => update(table, {auth: true, spare: true});
