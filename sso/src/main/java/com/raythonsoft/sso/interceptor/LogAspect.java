package com.raythonsoft.sso.interceptor;

import com.alibaba.fastjson.JSON;
import com.raythonsoft.common.util.NetworkUtil;
import com.raythonsoft.sso.model.Log;
import com.raythonsoft.sso.service.LogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * Created by Anur IjuoKaruKas on 2018/1/26.
 * Description :
 */
@Aspect
@Log4j
@Component
public class LogAspect {

    // FIXME: 2018/1/27 待优化 ，看起来好像没有什么实际作用

    @Autowired
    private LogService logService;

    @Pointcut("execution(* *..controller..*.*(..))")
    public void log() {
    }

    @Around("log()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Log ssoLog = Log.builder()
                .ip(NetworkUtil.getIpAddress(request))
                .method(request.getMethod())
                .time(new Date())
                .uri(request.getRequestURI())
                .userAgent(request.getHeader("User-Agent"))
                .username(JSON.toJSONString(request.getUserPrincipal())).build();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (method.isAnnotationPresent(ApiOperation.class)) {
            ssoLog.setDescription(method.getAnnotation(ApiOperation.class).value());
        }

        if (method.isAnnotationPresent(RequiresPermissions.class)) {
            RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
            String[] permissions = requiresPermissions.value();
            int length = permissions.length;
            StringBuilder permissionsStr = new StringBuilder();
            int count = 0;
            if (length > 0) {
                for (String permission : permissions) {
                    if (count != 0) {
                        permissionsStr.append(", ");
                    }
                    permissionsStr.append(permission);
                    count++;
                }
            }
            ssoLog.setPermissions(permissionsStr.toString());
        }

        Map paramMap = request.getParameterMap();
        if (paramMap.size() == 0) {
            ssoLog.setParam(request.getQueryString());
        } else {
            ssoLog.setParam(JSON.toJSONString(request.getParameterMap()));
        }
        logService.save(ssoLog);
        return joinPoint.proceed();
    }
}
