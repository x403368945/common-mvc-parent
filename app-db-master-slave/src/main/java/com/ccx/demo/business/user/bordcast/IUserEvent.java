package com.ccx.demo.business.user.bordcast;

import com.ccx.demo.business.user.entity.TabUser;
import com.google.common.eventbus.Subscribe;
import com.support.config.BusConfig.EventBusListener;
import com.utils.IJson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户操作广播事件
 *
 *
 * @author 谢长春 2018/12/16
 */
@EventBusListener
public interface IUserEvent {

    /**
     * 监听新增用户广播
     *
     * @param obj {@link UserNew}
     */
    @Subscribe
    void listener(final UserNew obj);

    /**
     * 监听修改用户姓名广播
     *
     * @param obj {@link NicknameUpdate}
     */
    @Subscribe
    void listener(final NicknameUpdate obj);

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    class UserNew implements IJson {
        public static UserNew of(final TabUser user) {
            return UserNew.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .build();
        }

        @Builder.Default
        private final String comment = "新增用户广播";
        /**
         * 新增用户ID
         */
        private Long id;
        /**
         * 新用户姓名【昵称】
         */
        private String nickname;

        @Override
        public String toString() {
            return json();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    class NicknameUpdate implements IJson {
        public static NicknameUpdate of(final TabUser user) {
            return NicknameUpdate.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .build();
        }

        @Builder.Default
        private final String comment = "修改用户姓名广播";
        /**
         * 修改用户ID
         */
        private Long id;
        /**
         * 修改用户姓名【昵称】
         */
        private String nickname;

        @Override
        public String toString() {
            return json();
        }
    }
}
