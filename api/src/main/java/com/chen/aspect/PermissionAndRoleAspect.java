package com.chen.aspect;

import com.chen.annotation.Permissions;
import com.chen.annotation.Roles;
import com.chen.configuration.Constant;
import com.chen.configuration.RedisTemplate;
import com.chen.exception.PermissionException;
import com.chen.exception.RoleShortageException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author CHEN
 */
@Aspect
@Component
public class PermissionAndRoleAspect {
    @Resource
    private RedisTemplate redisTemplate;

    @Before(value = "@annotation(roles)")
    public void roleCheckAspect( Roles roles){
        //获取当前方法需要的角色
        String[] needRoles = roles.value();
        //获取登陆用户的角色
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader(Constant.HAND_AUTHORIZATION);
        Set<String> keys = redisTemplate.keys(Constant.ROLE_PREFIX + "*:" + token);
        String roleKey =(String) keys.toArray()[0];
        List<String> hasRoles = redisTemplate.getObj(roleKey,new TypeReference<List<String>>() {
        });
        //判断时候拥有需要的角色
        boolean isFlag=false;
        for (String needRole : needRoles) {
            if (hasRoles.contains(needRole)){
                isFlag=true;
                break;
            }
        }
        if (!isFlag){
            throw new RoleShortageException("角色不足");
        }
    }
    @Before(value = "@annotation(permissions)")
    public void roleCheckAspect(Permissions permissions){
        //获取当前方法需要的角色
        String[] needPermissions = permissions.value();
        //获取登陆用户的角色
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader(Constant.HAND_AUTHORIZATION);
        Set<String> keys = redisTemplate.keys(Constant.PERM_PREFIX + "*:" + token);
        String permissionKey =(String) keys.toArray()[0];
        List<String> hasPermissions = redisTemplate.getObj(permissionKey,new TypeReference<List<String>>() {
        });
        //判断时候拥有需要的角色
        boolean isFlag=false;
        for (String needPermission : needPermissions) {
            if (hasPermissions.contains(needPermission)){
                isFlag=true;
                break;
            }
        }
        if (!isFlag){
            throw new PermissionException("权限不足");
        }
    }
}

