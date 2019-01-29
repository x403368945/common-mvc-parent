package com.boot.security.config.init;

import com.boot.security.config.init.AppConfig.App;
import com.boot.security.config.init.AppConfig.Path;
import com.support.config.InitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 初始化文件存储目录
 *
 * @author 谢长春
 */
@Component
@Slf4j
public class PathInitializer implements InitConfig.Initializer {

    @Override
    public void init() {
        log.info("\n┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 环境配置 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬\n{}\n┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 环境配置 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴",
                Stream.of(App.values())
                        .map(value -> String.format("%s【%s: %s】 : %s", value.name(), value.key, value.comment, value.value()))
                        .collect(Collectors.joining("\n"))
        );
        log.info("\n┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 初始化路径配置 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬\n{}\n┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 初始化路径配置 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴",
                Stream.of(Path.values())
                        .map(path -> String.format("%s【%s】：%s", path.name(), path.comment, path.fpath().mkdirs().absolute()))
                        .collect(Collectors.joining("\n"))
        );
    }
}