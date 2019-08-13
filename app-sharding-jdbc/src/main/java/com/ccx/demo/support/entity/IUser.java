package com.ccx.demo.support.entity;

import com.ccx.demo.business.user.dao.jpa.UserRepository;
import com.querydsl.core.annotations.QueryTransient;

import java.util.Optional;

import static com.ccx.demo.config.init.BeanInitializer.Beans.userRepository;

/**
 * 所有实体类，跟用户相关的可以实现此接口；此接口自带获取创建者和修改者的获取方法
 *
 * @author 谢长春 2017-9-26
 */
public interface IUser {
    /**
     * 创建者ID
     *
     * @return Long
     */
    default Long getCreateUserId() {
        return null;
    }

    /**
     * 创建者
     *
     * @return String
     */
    @QueryTransient
    default String getCreateUserName() {
        return Optional.ofNullable(getCreateUserId()).map(id -> userRepository.<UserRepository>get().getNickame(id)).orElse(null);
    }

    /**
     * 修改者ID
     *
     * @return Long
     */
    default Long getModifyUserId() {
        return null;
    }

    /**
     * 修改者
     *
     * @return String
     */
    @QueryTransient
    default String getModifyUserName() {
        return Optional.ofNullable(getModifyUserId()).map(id -> userRepository.<UserRepository>get().getNickame(id)).orElse(null);
    }

//    /**
//     * 创建时间
//     */
//    @NotNull(groups = {ISave.class})
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
//    private Timestamp createTime;
//    /**
//     * 创建用户ID
//     */
//    @NotNull(groups = {ISave.class})
//    @Positive
//    private Long createUserId;
//    /**
//     * 修改时间
//     */
//    @NotNull(groups = {ISave.class, IUpdate.class})
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
//    private Timestamp modifyTime;
//    /**
//     * 修改用户ID
//     */
//    @NotNull(groups = {ISave.class, IUpdate.class})
//    @Positive
//    private Long modifyUserId;
//    /**
//     * 是否逻辑删除（1、已删除， 0、未删除）
//     */
//    @Indexed
//    @NotNull(groups = {ISave.class})
//    private Radio deleted;
//
//    @Override
//    public String toString() {
//        return this.json();
//    }
}