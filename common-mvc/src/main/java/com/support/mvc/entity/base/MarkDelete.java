package com.support.mvc.entity.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.utils.util.Dates;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

import static com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat;

/**
 * 逻辑删除
 *
 * @author 谢长春 2020-03-01l
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ApiModel(description = "批量逻辑删除")
public class MarkDelete {

    @NotNull
    @ApiModelProperty(position = 1, value = "数据 id", example = "1", required = true)
    private Object id;
    @Size(min = 32, max = 32)
    @ApiModelProperty(position = 2, value = "数据 uid", example = "UUID32")
    private String uid;
    @ApiModelProperty(position = 3, value = "最后一次更新数据时间戳", example = "1583069285501")
    @JSONField(parseFeatures = AllowISO8601DateFormat)
    private Timestamp timestamp;

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false, deserialize = false)
    public Long getLongId() {
        return Long.parseLong(Objects.toString(id));
    }

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false, deserialize = false)
    public String getStringId() {
        return Objects.toString(id);
    }

    public static void main(String[] args) {
        System.out.println(
                JSON.toJSONString(MarkDelete.builder().timestamp(Dates.now().timestamp()).build())
        );
    }
}
