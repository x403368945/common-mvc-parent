package com.ccx.demo.business.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.ccx.demo.config.init.AppConfig.Path;
import com.ccx.demo.config.init.AppConfig.URL;
import com.utils.IJson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户头像文件对象
 *
 * @author 谢长春 on 2017/10/17.
 */
@ApiModel(description = "用户头像文件对象")
public class UserFileInfo extends FileInfo implements IJson {

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false, deserialize = false)
    public String getPath() {
        return "/app/path";
//        return StringUtils.isEmpty(uname) ? null : Path.USER.absolute(uname);
    }

    public String getUrl() {
        return "http://url";
//        return StringUtils.isEmpty(uname) ? null : URL.USER.append(uname);
    }

}
