package com.ccx.demo.business.example.vo;

import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.example.entity.QTabDemoList;
import com.ccx.demo.business.example.entity.TabDemoList;
import com.ccx.demo.business.example.enums.DemoStatus;
import com.ccx.demo.business.user.entity.TabUser;
import com.utils.util.Dates;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

import static com.ccx.demo.business.example.entity.QTabDemoList.tabDemoList;

/**
 * <pre>
 * 实体：扩展 {@link TabDemoList}，增强单个实体的特性，同时满足 join 表的需求
 * 缺点在于 VO 类无法使用全部的 lombok 注解， VO 类继承后必须使用以下 3 个注解
 *
 * *@Data
 * *@EqualsAndHashCode(callSuper = true)
 * *@ToString(callSuper = true)
 * </pre>
 *
 * @author 谢长春 2020-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@ApiModel(description = "扩展：测试案例表：TabDemoList")
@JSONType(orders = {"id", "uid", "name", "content", "amount", "status", "insertTime", "insertUserId", "insertUserName", "updateTime", "updateUserId", "updateUserName", "deleted", "insertUser", "updateUser"})
public class TabDemoListVO extends TabDemoList {

    private static final long serialVersionUID = -7251222373656975725L;
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

    @Override
    public QdslWhere where() {
        final QTabDemoList q = tabDemoList;
        // 增强扩展查询条件
        return super.where()
                .andIfNonEmpty(statusArray, () -> q.status.in(statusArray))
                .and(updateTimeRange, () -> q.updateTime.between(updateTimeRange.rebuild().getBegin(), updateTimeRange.getEnd()));

    }
}
