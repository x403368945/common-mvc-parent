package com.ccx.demo.business.user.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.user.enums.AuthorityCode;
import com.ccx.demo.business.user.enums.AuthorityType;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;


/**
 * <pre>
 * 菜单及操作权限
 *
 * @author 谢长春 2019/8/27
 */
@Data
@Accessors(chain = true)
@JSONType(orders = {"code", "parentCode", "name", "icon", "type", "url", "nodes"})
public class Authority implements Cloneable, Serializable {
    /**
     * Controller 方法头上的注解权限代码
     */
    private AuthorityCode code;
    /**
     * 父级代码
     */
    private AuthorityCode parentCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 权限类型
     */
    private AuthorityType type;
    /**
     * 菜单路由地址 route，可选参数，需要跟前端约定
     */
    private String route;
    /**
     * 权限是否被选中
     */
    private boolean checked;
    /**
     * 子节点
     */
    private List<Authority> nodes;

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
