package com.chen.aspect;

import com.chen.annotation.Log;
import com.chen.configuration.RedisTemplate;
import com.chen.entity.OperLog;
import com.chen.service.OperLogService;
import com.chen.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * @author CHEN
 */
@Component
@Aspect
@Slf4j
public class LogAspect  {
    @Resource
    private OperLogService operLogService;

    @Resource
    private RedisTemplate redisTemplate;



    @AfterReturning("@annotation(oLog)")
    public void afterReturning(JoinPoint joinPoint, Log oLog) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        System.out.println("main____________"+Thread.currentThread().getName());
        OperLog operLog = createOperLog(joinPoint, request, oLog, null);
        operLogService.insert(operLog);
        log.info("{},执行了【{}】操作",operLog.getOperName(),operLog.getTitle());
    }

    @AfterThrowing(value = "@annotation(oLog)", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Log oLog, Exception exception) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        System.out.println("main____________"+Thread.currentThread().getName());
        OperLog operLog = createOperLog(joinPoint, request, oLog, exception);
        operLogService.insert(operLog);
        log.error("{},执行了【{}】操作",operLog.getOperName(),operLog.getTitle(),exception);
    }

//    private void logHandler(JoinPoint joinPoint,HttpServletRequest request,Log log,Exception exception){
//        //根据现场信息封装日志实例
//        OperLog operLog=new OperLog();
//        operLog.setTitle(log.title());
//        operLog.setBusinessType(log.businessType());
//        if (exception!=null){
//            operLog.setErrormsg(exception.getMessage().length()>1000?exception.getMessage().substring(0,1000):exception.getMessage());
//            operLog.setStatus(500);
//        }else {
//            operLog.setStatus(200);
//        }
//        operLog.setOperIp(request.getRemoteAddr());
//        operLog.setRequestMethod(request.getMethod());
//        AuthUtil.getLoginUser(redisTemplate);
//        if (AuthUtil.getLoginUser(redisTemplate).getUser() != null){
//            operLog.setOperName(AuthUtil.getLoginUser(redisTemplate).getUser().getUserName());
//        }
//        operLog.setOperUrl(request.getRequestURI());
//        operLog.setMethod(joinPoint.getSignature().getName());
//        operLog.setOpertime(new Date());
//        //提交任务
//        executorService.execute(()->{
//            //保存日志对着
//            operLogService.insert(operLog);
//        });
//
//    }

    /**
     * 数据库添加日志
     */
//    @Async("chen_logger")
//    public void logHandler(OperLog operLog) {
//        System.out.println("log____________"+Thread.currentThread().getName());
//        //保存日志对着
//        operLogService.insert(operLog);
//    }
    private OperLog createOperLog(JoinPoint joinPoint, HttpServletRequest request, Log log, Exception exception){
        //根据现场信息封装日志实例
        OperLog operLog = new OperLog();
        operLog.setTitle(log.title());
        operLog.setBusinessType(log.businessType());
        if (exception != null) {
            operLog.setErrormsg(exception.getMessage().length() > 1000 ? exception.getMessage().substring(0, 1000) : exception.getMessage());
            operLog.setStatus(500);
        } else {
            operLog.setStatus(200);
        }
        operLog.setOperIp(request.getRemoteAddr());
        operLog.setRequestMethod(request.getMethod());
        AuthUtil.getLoginUser(redisTemplate);
        if (AuthUtil.getLoginUser(redisTemplate).getUser() != null) {
            operLog.setOperName(AuthUtil.getLoginUser(redisTemplate).getUser().getUserName());
        }
        operLog.setOperUrl(request.getRequestURI());
        operLog.setMethod(joinPoint.getSignature().getName());
        operLog.setOpertime(new Date());
        return operLog;
    }


}
