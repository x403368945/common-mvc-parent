/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const save = (table, {auth = false, spare = false}) => {
  const {
    comment,
    date,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? 'final TabUser user, ' : '';
  const authUserId = auth ? ', user.getId()' : '';
  return `${spareBegin}
    @PostMapping
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_save')") // <
    @ApiOperation(value = "1.新增${comment}", tags = {"${date}"})
    @ApiImplicitParam(name = "body", dataType = "${TabName}", dataTypeClass = ${TabName}.class, required = true)
    @ApiOperationSupport(
            order = 1,
            ignoreParameters = {
                    "body.id", "body.uid", "body.deleted", "body.insertTime", "body.insertUserId", "body.insertUserName", "body.updateTime", "body.updateUserId", "body.updateUserName", "body.timestamp", "body.sorts"
            })
    @ResponseBody
    @Override
    public Result<${TabName}> save(${authUser}final String body) {
        return new Result<${TabName}>().execute(result -> result.setSuccess(
                service.save(JSON.parseObject(body, ${TabName}.class)${authUserId})
        ));
    }
${spareEnd}`
};
export const saveOpen = table => save(table, {auth: false, spare: false});
export const saveOpenSpare = table => save(table, {auth: false, spare: true});

export const saveAuth = table => save(table, {auth: true, spare: false});
export const saveAuthSpare = table => save(table, {auth: true, spare: true});
