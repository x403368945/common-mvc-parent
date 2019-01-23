package com.demo.config.init;

import com.config.InitConfig;
import com.demo.config.init.AppConfig.App;
import com.demo.config.init.AppConfig.Path;
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
    public int priority() {
        return 1;
    }

    @Override
    public void init() {
        log.info("\n┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 环境配置 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬\n{}\n┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 环境配置 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴",
                Stream.of(App.values())
                        .map(value -> String.format("%s【%s: %s】 : %s", value.name(), value.key, value.comment, value.value()))
                        .collect(Collectors.joining("\n"))
        );
        log.info("┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 初始化路径配置 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬");
        log.info("\n{}",Stream.of(Path.values())
                .map(path -> String.format("%s【%s】：%s", path.name(), path.comment, path.fpath().mkdirs().absolute()))
                .collect(Collectors.joining("\n"))
        );
        log.info("┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 初始化路径配置 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴");
    }
}