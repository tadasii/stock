package com.zz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhangzheng on 2019/10/24.
 */
@Controller
public class IndexController {
    @RequestMapping("")
    public String hello(){
        System.out.println("hello");
        return "/index";
    }
    @RequestMapping("/index")
    public String index(){
        return "/index";
    }

}
