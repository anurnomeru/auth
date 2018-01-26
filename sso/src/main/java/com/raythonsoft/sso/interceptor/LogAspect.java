package com.raythonsoft.sso.interceptor;

import com.alibaba.fastjson.JSON;
import com.raythonsoft.repository.model.Log;
import com.raythonsoft.repository.service.LogService;
import com.raythonsoft.repository.util.NetworkUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by Anur IjuoKaruKas on 2018/1/26.
 * Description :
 */
@Aspect
@Log4j
public class LogAspect {

    @Autowired
    private LogService logService;

    @Around("execution(* *..controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Object result = joinPoint.proceed();

        Log log = Log.builder()
                .ip(NetworkUtil.getIpAddress(request))
                .method(request.getMethod())
                .result(JSON.toJSONString(request))
                .operationTime(new Date())
                .uri(request.getRequestURI())
                .userAgent(request.getHeader("User-Agent"))
                .username(JSON.toJSONString(request.getUserPrincipal())).build();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            log.setDescription(method.getAnnotation(ApiOperation.class).value());
        }
    }
}
