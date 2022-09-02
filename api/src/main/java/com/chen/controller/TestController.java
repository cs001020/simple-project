package com.chen.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CHEN
 */
@RestController
@CrossOrigin
public class TestController {
    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }
}
