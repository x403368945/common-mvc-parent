package com.ccx.demo.business.common.web;

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
@Slf4j
public class HomeController
        extends AbstractMvcConfig.ErrorController {
    public HomeController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(value = "/", method = {GET, POST, PUT, PATCH, DELETE})
    public String home() {
        return "redirect:/doc.html#/plus";
    }
}
