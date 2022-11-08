package com.ymx.viewlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ViewlogController {

    @RequestMapping("/")
    public String root(){
        String profile = System.getProperty("spring.profiles.active");
        System.out.println(">root>profile:"+profile);
        log.trace("trace test");
		log.debug("debug test");
		log.info("info test");
		log.warn("warn test");
		log.error("error test");
        return "index";
    }

    @RequestMapping("/errorlog")
    public String index(){
        log.error("error log test!");
        return "index";
    }
    
}
