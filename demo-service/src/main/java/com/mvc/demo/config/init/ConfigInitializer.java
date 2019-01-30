package com.mvc.demo.config.init;

import com.support.config.InitConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 初始化文件存储目录
 *
 * @author 谢长春
 */
@Component
@Slf4j
public class ConfigInitializer implements InitConfig.Initializer {
    private static final Properties properties = new Properties(); // 创建资源对象

    @SneakyThrows
    @Override
    public void init() {
        log.info("┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 服务参数配置 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬");
        properties.load(new InputStreamReader(
                ConfigInitializer.class.getResourceAsStream("/demo-service.properties"), UTF_8
        ));
        final String catalinaBase = Optional.ofNullable(System.getProperty("catalina.base")).orElse("");
        final String catalinaHome = Optional.ofNullable(System.getProperty("catalina.home")).orElse("");
        properties.forEach((key, value) ->
                properties.setProperty(Objects.toString(key), Objects.toString(value)
                        .replace("${catalina.base}", catalinaBase)
                        .replace("${catalina.home}", catalinaHome)
                )
        );
        log.info("{}", properties.entrySet().stream()
                .map(entry -> String.format("%s:%s", entry.getKey().toString(), entry.getValue().toString()))
                .collect(Collectors.joining("\n"))
        );
        log.info("┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 服务参数配置 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴");
    }
}