package com.chen.controller;

import com.chen.annotation.Log;
import com.chen.entity.LoginUser;
import com.chen.entity.User;
import com.chen.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author CHEN
 */
@Slf4j
@RestController
public class IndexController {
    @Resource
    private UserService userService;

    /**
     * 登陆认证
     * @param user 用户
     * @return 结果
     */
    @PostMapping("/login")
    public ResponseEntity<LoginUser> login(@RequestBody @Validated User user, BindingResult bindingResult){
        //处理不合法的数据
        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error->log.error("登陆时用户校验失败:{}",error.getDefaultMessage()));
            return ResponseEntity.status(500).build();
        }
        LoginUser loginUser= userService.login(user.getUserName(),user.getPassword());
        return ResponseEntity.ok().body(loginUser);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logOut(){
        try {
            userService.logOut();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().body("退出成功");
    }

}

