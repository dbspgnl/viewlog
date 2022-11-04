package com.ymx.viewlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ViewlogController {

    @RequestMapping("/")
    public String root(){
        return "index";
    }

    @RequestMapping("/errorlog")
    public String index(){
        log.error("error log test!");
        return "index";
    }
    
}
