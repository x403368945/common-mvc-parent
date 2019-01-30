package com.boot.demo.config.init;

import com.utils.IJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
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
    private String markdown;
    private String pathRoot;
    private boolean debug;
    private boolean junit;

    @Override
    public String toString() {
        return json();
    }
}