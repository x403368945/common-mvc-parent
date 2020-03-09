package com.ccx.demo.business.example.entity;

import com.alibaba.fastjson.annotation.JSONType;
import com.support.mvc.entity.ITable;
import com.support.mvc.entity.validated.IMarkDelete;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import com.utils.IJson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;

/**
 * @author 谢长春 2018/12/20
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Slf4j
@JSONType(orders = {"id", "uid", "label", "value", "content"})
public class TabValid implements ITable, IJson {

    private static final long serialVersionUID = 1494537060273446575L;
    @NotNull(groups = {IUpdate.class, IMarkDelete.class})
    @Positive
    private Long id;
    @NotBlank(groups = {IUpdate.class, IMarkDelete.class})
    @Size(min = 32, max = 32)
    private String uid;
    @NotBlank(groups = {ISave.class, IUpdate.class})
    @Size(max = 10)
    private String label;
    @NotNull(groups = {ISave.class, IUpdate.class})
    @PositiveOrZero
    private Short value;
    @Size(max = 30)
    private String content;

    public TabValid log() {
        log.debug(this.toString());
        return this;
    }

}