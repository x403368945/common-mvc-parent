package com.security.config.init;

import com.config.Initializer;
import com.security.config.init.AppConfig.Path;
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
    public void init() {
        log.info("┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 初始化路径配置 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬");
        log.info(Stream.of(Path.values())
                .map(path -> String.format("%s【%s】：%s", path.name(), path.comment, path.fpath().mkdirs().absolute()))
                .collect(Collectors.joining("\n"))
        );
        log.info("┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 初始化路径配置 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴");
    }
}