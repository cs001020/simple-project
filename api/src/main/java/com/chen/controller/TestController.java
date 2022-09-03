package com.chen.controller;

import com.chen.configuration.RedisTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author CHEN
 */

//@CrossOrigin
@Slf4j
@RestController
public class TestController {
    @Resource
    private RedisTemplate redisTemplate;
    @GetMapping("/test")
    public String test(){
        redisTemplate.setObj("map", Arrays.asList("张三","李四","王五"),200L);
        List<String> map = redisTemplate.getObj("map", new TypeReference<List<String>>() {});
        log.info(map.toString());
        return "Hello World";
    }
}
