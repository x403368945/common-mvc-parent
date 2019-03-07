package com.ccx.socket.config;

import com.support.config.AbstractMvcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * spring-boot 特殊处理：大部分配置在 application.yml 中，简化配置
 * Spring MVC 配置
 * 参考 ： https://linesh.gitbooks.io/spring-mvc-documentation-linesh-translation/content/
 * Spring Thymeleaf 配置
 * 参考 ： https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#spring-mvc-configuration
 *
 * @author 谢长春 2018-10-3
 */
@Configuration
@Slf4j
public class WebMvcConfig extends AbstractMvcConfig {
}
