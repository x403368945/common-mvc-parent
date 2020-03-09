package com.ccx.demo.business.user.vo;

import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.user.entity.TabUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户实体
 *
 * @author 谢长春
 */
@ApiModel(description = "扩展用户实体VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JSONType(
        ignores = {"password"}, // 序列化时忽略属性，反序列化不受影响
        orders = {"id", "uid", "subdomain", "username", "nickname", "phone", "email", "role", "registerSource", "deleted"}
)
public class TabUserVO extends TabUser {
    @SneakyThrows
    public static TabUserVO ofTabUser(final TabUser entity) {
        final TabUserVO vo = new TabUserVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    /**
     * 权限指令集合
     */
    @ApiModelProperty(value = "权限指令代码集合")
    private List<String> authorityList;

    public List<String> getAuthorityList() {
        if (CollectionUtils.isEmpty(authorityList)) {
            authorityList = Stream.of(Objects.requireNonNull(getRoles(), "当前登录账户未配置权限"))
                    .flatMap(id -> Stream.of(getRoleAuthoritiesCacheById(id)))
                    .distinct()
                    .collect(Collectors.toList());
        }
        return authorityList;
    }
}
