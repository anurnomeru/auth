package com.raythonsoft.sso.interceptor;

import com.alibaba.fastjson.JSON;
import com.raythonsoft.common.util.NetworkUtil;
import com.raythonsoft.sso.model.SsoLog;
import com.raythonsoft.sso.service.SsoLogService;
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
    private SsoLogService ssoLogService;

    @Pointcut("execution(* *..controller..*.*(..))")
    public void log() {
    }

    @Around("log()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        SsoLog ssoLog = SsoLog.builder()
                .requestIp(NetworkUtil.getIpAddress(request))
                .requestMethod(request.getMethod())
                .requestTime(new Date())
                .requestUri(request.getRequestURI())
                .userAgent(request.getHeader("User-Agent"))
                .userName(JSON.toJSONString(request.getUserPrincipal())).build();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (method.isAnnotationPresent(ApiOperation.class)) {
            ssoLog.setMethodDescription(method.getAnnotation(ApiOperation.class).value());
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
            ssoLog.setMethodPermissions(permissionsStr.toString());
        }

        Map paramMap = request.getParameterMap();
        if (paramMap.size() == 0) {
            ssoLog.setRequestParam(request.getQueryString());
        } else {
            ssoLog.setRequestParam(JSON.toJSONString(request.getParameterMap()));
        }
        ssoLogService.save(ssoLog);
        return joinPoint.proceed();
    }
}
