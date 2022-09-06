package com.chen.util;

import com.chen.configuration.Constant;
import com.chen.configuration.RedisTemplate;
import com.chen.entity.LoginUser;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;

/**
 * @author CHEN
 */
public class AuthUtil {
    //获取当前的登陆对象
    public static LoginUser getLoginUser(RedisTemplate redisTemplate){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader(Constant.HAND_AUTHORIZATION);
        if (token==null){
            throw new RuntimeException("当前用户未登陆");
        }
        //使用token去redis中查看有没对应的key
        Set<String> keys = redisTemplate.keys(Constant.TOKEN_KEY + "*" + token);
        if (keys==null||keys.size()==0){
            throw new RuntimeException("当前用户未登陆");
        }
        String key = (String) keys.toArray()[0];
        //使用token去redis中查看有没对应的loginUser
        LoginUser loginUser = redisTemplate.getObj(key, new TypeReference<LoginUser>() {});
        if (loginUser==null){
            throw new RuntimeException("当前用户未登陆");
        }
        return loginUser;
    }
}
