package com.ccx.security.business.common.web;

import com.support.config.AbstractMvcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * <p>
 * spring-boot 特殊处理：继承 {@link com.support.config.AbstractMvcConfig.ErrorController} 处理异常
 *
 * @author 谢长春
 */
@Controller
@RequestMapping("")
@Slf4j
public class HomeController
// spring-boot start >> mvc 不需要继承 {@link AbstractMvcConfig.ErrorController}
        extends AbstractMvcConfig.ErrorController
// spring-boot end <<<<
{
    // spring-boot start >> mvc 不需要继承 {@link AbstractMvcConfig.ErrorController}
    public HomeController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }
    // spring-boot end <<<<

    @RequestMapping(value = "/", method = {GET, POST, PUT, PATCH, DELETE})
    public String home() {
        return "/static/index";
    }
}