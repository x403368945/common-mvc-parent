package com.ccx.demo.business.common.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.ccx.demo.config.init.AppConfig.Path;
import com.ccx.demo.config.init.AppConfig.URL;
import com.support.mvc.entity.base.Prop;
import com.utils.IJson;
import com.utils.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.support.mvc.entity.base.Prop.Type.STRING;

/**
 * 上传文件对象
 *
 *
 * @author 谢长春 on 2017/10/17.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Accessors(chain = true)
public class FileInfo implements IJson {
    /**
     * 实体类所有属性名
     */
    public enum Props {
        name(STRING.build(true, "文件名，用户上传的文件名")),
        uname(STRING.build(true, "唯一文件名，磁盘上存储的uuid文件名")),
        url(STRING.build(true, "本次上传文件临时访问路径")),;
        private final Prop prop;

        public Prop getProp() {
            return prop;
        }

        Props(final Prop prop) {
            prop.setName(this.name());
            this.prop = prop;
        }

        public static List<Prop> list() {
            return Stream.of(Props.values()).map(Props::getProp).collect(Collectors.toList());
        }
    }

    /**
     * 文件名，用户上传的文件名
     */
    protected String name;
    /**
     * 唯一文件名，磁盘上存储的uuid文件名
     */
    protected String uname;

    /**
     * 文件默认存在临时目录
     *
     * @return String 文件绝对路径
     */
    @JSONField(serialize = false, deserialize = false)
    public String getPath() {
        return Util.isEmpty(uname) ? null : Path.TEMP.absolute(uname);
    }

    /**
     * 文件访问路径， 示例：http://127.0.0.1/app/test.xlsx
     *
     * @return String
     */
    public String getUrl() {
        return Util.isEmpty(uname) ? null : URL.TEMP.append(uname);
    }

    public <T> T parse(Class<T> clazz) {
        return JSON.parseObject(json(), clazz);
    }
}
