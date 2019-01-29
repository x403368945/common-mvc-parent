package com.boot.security.business.common.web;

import com.support.config.AbstractMvcConfig.ErrorController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 谢长春
 */
@Controller
@RequestMapping("")
@Slf4j
public class HomeController extends ErrorController {
    public HomeController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

//    @RequestMapping(value = "/", method = {GET, POST, PUT, PATCH, DELETE})
//    public String home() {
//        return "/static/index";
//    }
}