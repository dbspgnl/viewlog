package com.ymx.viewlog.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ymx.viewlog.service.ViewlogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewlogController {

    private final ViewlogService viewlogService;

    @RequestMapping("/")
    public String root(){
        return "index";
    }

    @RequestMapping("/errorlog")
    public String index(){
        log.error("error log test!");
        return "index";
    }

    @RequestMapping("/isRemoting")
    public ResponseEntity<?> isRemoting(){
        Boolean result = false;
        result = viewlogService.isRemoting();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping("/ssh")
    public ResponseEntity<?> ssh(){
        return ResponseEntity.status(HttpStatus.OK).body(viewlogService.ssh());

    }

    @RequestMapping("/log")
    public ResponseEntity<?> log(){
        return ResponseEntity.status(HttpStatus.OK).body(viewlogService.log());
    }

    @RequestMapping("/getServerList")
    public ResponseEntity<?> getServer(){
        return ResponseEntity.status(HttpStatus.OK).body(viewlogService.getServerList());
    }

    @RequestMapping("/addServer")
    public ResponseEntity<?> addServer(@RequestParam Map<String, String> formData){
        return ResponseEntity.status(HttpStatus.OK).body(viewlogService.addServer(formData));
    }
    
}
