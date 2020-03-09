package com.ccx.demo.business.user.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.user.enums.AuthorityCode;
import com.ccx.demo.business.user.enums.AuthorityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

/**
 * <pre>
 * 菜单及操作权限
 *
 * @author 谢长春 2019/8/27
 */
@ApiModel(description = "菜单及操作权限指令对象")
@Data
@Accessors(chain = true)
@JSONType(orders = {"ordinal", "code", "parentCode", "name", "icon", "type", "route", "checked", "nodes"})
public class Authority implements Cloneable, Serializable {
    /**
     * Controller 方法头上的注解权限代码
     */
    @ApiModelProperty(position = 2, value = "指令代码")
    private AuthorityCode code;
    /**
     * 父级代码
     */
    @ApiModelProperty(position = 3, value = "父节点代码")
    private AuthorityCode parentCode;
    /**
     * 名称
     */
    @ApiModelProperty(position = 4, value = "名称")
    private String name;
    /**
     * 图标
     */
    @ApiModelProperty(position = 5, value = "图标")
    private String icon;
    /**
     * 权限类型
     */
    @ApiModelProperty(position = 6, value = "权限类型")
    private AuthorityType type;
    /**
     * 菜单路由地址 route，可选参数，需要跟前端约定
     */
    @ApiModelProperty(position = 7, value = "菜单路由地址 route，可选参数，需要跟前端约定")
    private String route;
    /**
     * 权限是否被选中
     */
    @ApiModelProperty(position = 8, value = "权限是否被选中")
    private boolean checked;
    /**
     * 子节点
     */
    @ApiModelProperty(position = 9, value = "子节点")
    private Set<Authority> nodes;

    /**
     * 指令序号
     *
     * @return {@link Integer}
     */
    @ApiModelProperty(position = 1, value = "序号，随着插入节点，指令序号是会变化的，这里只用作排序")
    public Integer getOrdinal() {
        return Optional.ofNullable(code).map(Enum::ordinal).orElse(null);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @SneakyThrows
    @Override
    public Authority clone() {
        return (Authority) super.clone();
    }
}
