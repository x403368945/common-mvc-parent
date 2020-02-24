package com.ccx.demo.business.common.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.ccx.demo.config.init.AppConfig.Path;
import com.ccx.demo.config.init.AppConfig.URL;
import com.utils.IJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * 上传文件对象
 *
 * @author 谢长春 on 2017/10/17.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Accessors(chain = true)
@Api(tags = "上传文件对象")
public class FileInfo implements IJson {

    /**
     * 文件名，用户上传的文件名
     */
    @ApiModelProperty(position = 1, value = "文件名，用户上传的文件名", example = "测试.png")
    protected String name;
    /**
     * 唯一文件名，磁盘上存储的uuid文件名
     */
    @ApiModelProperty(position = 2, value = "唯一文件名，磁盘上存储的uuid文件名", example = "{uuid}.png")
    protected String uname;

    /**
     * 文件默认存在临时目录
     *
     * @return String 文件绝对路径
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false, deserialize = false)
    public String getPath() {
        return StringUtils.isEmpty(uname) ? null : Path.TEMP.absolute(uname);
    }

    /**
     * 文件访问路径， 示例：http://127.0.0.1/app/test.xlsx
     *
     * @return String
     */
    @ApiModelProperty(position = 3, value = "文件访问路径", example = "http://127.0.0.1/app/{uuid}.png")
    @JSONField(serialize = false, deserialize = false)
    public String getUrl() {
        return StringUtils.isEmpty(uname) ? null : URL.TEMP.append(uname);
    }

    public <T> T parse(Class<T> clazz) {
        return JSON.parseObject(json(), clazz);
    }

}
