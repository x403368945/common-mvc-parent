package com.ccx.demo.config.init;

import com.support.config.InitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 初始化配置信息
 *
 * @author 谢长春
 */
@Component
@Slf4j
public class DemoServiceConfigInitializer implements InitConfig.Initializer {
    @Autowired
    private DemoServiceAppProperties properties;

    @Override
    public void init() {
        log.info("┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 服务参数配置 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬");
        log.info("\n{}", properties.toString());
        log.info("┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 服务参数配置 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴");
    }
}