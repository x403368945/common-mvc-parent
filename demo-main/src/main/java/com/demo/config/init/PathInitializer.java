package com.demo.config.init;

import com.config.Initializer;
import com.demo.config.init.AppConfig.App;
import com.demo.config.init.AppConfig.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 初始化文件存储目录
 */
@Component
@Slf4j
public class PathInitializer implements Initializer {
    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void init() {
        log.info("┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 环境配置 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬");
        log.info("{}",
                Stream.of(App.values())
                        .map(value -> String.format("%s【%s: %s】 : %s", value.name(), value.key, value.comment, value.value()))
                        .collect(Collectors.joining("\n"))
        );
        log.info("┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 环境配置 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴");
        log.info("┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 初始化路径配置 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬");
        log.info(Stream.of(Path.values())
                .map(path -> String.format("%s【%s】：%s", path.name(), path.comment, path.fpath().mkdirs().absolute()))
                .collect(Collectors.joining("\n"))
        );
        log.info("┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 初始化路径配置 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴");
    }
}