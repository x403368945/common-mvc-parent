package com.boot.socket.common.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 *
 * @author 谢长春 2018/12/2
 */
@RequestMapping("/")
@Controller
@Slf4j
public class HomeController {
    @RequestMapping(method = {POST, GET, PUT, PATCH, DELETE})
    public String home() {
        return "/static/index";
    }
}
