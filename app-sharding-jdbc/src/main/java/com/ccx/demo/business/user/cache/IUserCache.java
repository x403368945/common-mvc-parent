package com.ccx.demo.business.user.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.ccx.demo.business.user.dao.jpa.UserRepository;
import com.ccx.demo.business.user.entity.TabUser;
import com.querydsl.core.annotations.QueryTransient;

import java.beans.Transient;
import java.util.Optional;

import static com.ccx.demo.config.init.BeanInitializer.Beans.userRepository;

/**
 * 所有实体类，跟用户相关的可以实现此接口；此接口自带获取创建者和修改者的获取方法
 *
 * @author 谢长春 2017-9-26
 */
public interface IUserCache {
    /**
     * 获取缓存用户昵称
     *
     * @param userId {@link TabUser#getId()}
     * @return {@link String}
     */
    @Transient
    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    default String getNickNameCacheById(final Long userId) {
        return Optional.ofNullable(userId).map(id -> userRepository.<UserRepository>get().getNickame(id)).orElse(null);
    }

    /**
     * 创建者ID
     *
     * @return Long
     */
    default Long getInsertUserId() {
        return null;
    }

    /**
     * 创建者
     *
     * @return String
     */
    @Transient
    @QueryTransient
    default String getInsertUserName() {
        return getNickNameCacheById(getInsertUserId());
    }

    /**
     * 修改者ID
     *
     * @return Long
     */
    default Long getUpdateUserId() {
        return null;
    }

    /**
     * 修改者
     *
     * @return String
     */
    @Transient
    @QueryTransient
    default String getUpdateUserName() {
        return getNickNameCacheById(getUpdateUserId());
    }

//    /**
//     * 创建时间
//     */
//    @NotNull(groups = {ISave.class})
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
//    private Timestamp insertTime;
//    /**
//     * 创建用户ID
//     */
//    @NotNull(groups = {ISave.class})
//    @Positive
//    private Long insertUserId;
//    /**
//     * 修改时间
//     */
//    @NotNull(groups = {ISave.class, IUpdate.class})
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
//    private Timestamp updateTime;
//    /**
//     * 修改用户ID
//     */
//    @NotNull(groups = {ISave.class, IUpdate.class})
//    @Positive
//    private Long updateUserId;
//    /**
//     * 是否逻辑删除（1、已删除， 0、未删除）
//     */
//    @Indexed
//    @NotNull(groups = {ISave.class})
//    private Radio deleted;
//
}