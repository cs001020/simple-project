package com.chen.intercept;

import com.chen.configuration.Constant;
import com.chen.configuration.CustomObjectMapper;
import com.chen.configuration.RedisTemplate;
import com.chen.entity.LoginUser;
import com.fasterxml.jackson.core.type.TypeReference;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;


/**
 * @author CHEN
 */

public class LoginIntercept implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    CustomObjectMapper objectMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResponseEntity<String> responseEntity = ResponseEntity.status(401).body("错误的凭证");
        //如果不是白名单的请求，获取请求头的“Authorization”
        String token = request.getHeader(Constant.HAND_AUTHORIZATION);
        if (token==null){
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(responseEntity));
            return false;
        }
        //使用token去redis中查看有没对应的key
        Set<String> keys = redisTemplate.keys(Constant.TOKEN_KEY + "*" + token);
        if (keys==null||keys.size()==0){
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(responseEntity));
            return false;
        }
        String key = (String) keys.toArray()[0];
        //使用token去redis中查看有没对应的loginUser
        LoginUser loginUser = redisTemplate.getObj(key, new TypeReference<LoginUser>() {});
        if (loginUser==null){
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(responseEntity));
            return false;
        }
        //续命
        redisTemplate.expire(key,Constant.TOKEN_TIME);
        return true;
    }
}
