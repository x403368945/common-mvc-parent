package com.ccx.demo.business.example.vo;

import com.ccx.demo.business.example.entity.DemoMongo;
import com.ccx.demo.business.example.enums.DemoStatus;
import com.ccx.demo.business.user.entity.TabUser;
import com.utils.util.Dates;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class DemoMongoVO extends DemoMongo {
    public DemoMongoVO(DemoMongo parent) {
        super();
    }

    /**
     * 查询参数：状态集合
     */
    @ApiModelProperty(value = "查询参数：状态集合。com.ccx.demo.business.example.enums.DemoStatus")
    private Set<DemoStatus> statusArray;
    /**
     * 更新时间查询区间
     */
    @ApiModelProperty(value = "查询参数：更新时间查询区间")
    private Dates.Range updateTimeRange;

    /**
     * VO 类独立的特性，不会影响到数据表实体
     *
     * @return {@link TabUser}
     */
    @ApiModelProperty(value = "扩展参数：新增用户信息")
    public TabUser getInsertUser() {
        return getTabUserCacheById(getInsertUserId()).orElse(null);
    }

    /**
     * VO 类独立的特性，不会影响到数据表实体
     *
     * @return {@link TabUser}
     */
    @ApiModelProperty(value = "扩展参数：修改用户信息")
    public TabUser getUpdateUser() {
        return getTabUserCacheById(getUpdateUserId()).orElse(null);
    }

}
