package com.ccx.demo.config.init;

import com.utils.IJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 全局应用配置
 * // spring-boot start >> spring-boot 使用 yml 文件，可以通过 @ConfigurationProperties 注解，然后在 Application.java 依赖之后，不在需要 @Value 注解
 * // spring-mvc start >> spring-mvc 需要依赖 @Value 注解声明 application.properties 中的属性
 *
 * @author 谢长春 2019/1/22
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class AppProperties implements IJson {
    @Value("${app.env}")
    private String env;
    @Value("${app.admin_user_id}")
    private long adminUserId;
    @Value("${app.admin_user}")
    private String adminUser;
    @Value("${app.ip}")
    private String ip;
    @Value("${app.domain}")
    private String domain;
    @Value("${app.markdown}")
    private String markdown;
    @Value("${app.path_root}")
    private String pathRoot;
    @Value("${app.debug}")
    private boolean debug;
    @Value("${app.junit}")
    private boolean junit;

    @Override
    public String toString() {
        return json();
    }
}