package com.ccx.security.config.init;

import com.utils.IJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局配置文件
 * // spring-boot start >> spring-boot 使用 yml 文件，可以通过 @ConfigurationProperties 注解，然后在 Application.java 依赖之后，不在需要 @Value 注解
 * // spring-mvc start >> spring-mvc 需要依赖 @Value 注解声明 application.properties 中的属性
 *
 * @author 谢长春 2019/1/22
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@ConfigurationProperties("app")
public class AppProperties implements IJson {
    private String env;
    private long adminUserId;
    private String adminUser;
    private String ip;
    private String domain;
    private String pathRoot;
    private Integer tokenExpired;

}