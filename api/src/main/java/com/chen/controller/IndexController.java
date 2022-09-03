package com.chen.controller;

import com.chen.entity.LoginUser;
import com.chen.entity.User;
import com.chen.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
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
     * @param user
     * @return
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

}

