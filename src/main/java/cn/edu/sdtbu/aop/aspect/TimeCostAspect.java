package cn.edu.sdtbu.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-12 08:35
 */
@Slf4j
@Aspect
public class TimeCostAspect {
    @Around("execution(* cn.edu.sdtbu.controller.api.*.*(..))")
    public ResponseEntity around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        long before = System.currentTimeMillis();
        ResponseEntity res = (ResponseEntity) joinPoint.proceed();
        long after = System.currentTimeMillis();
        log.debug("{}: {} was cost [ {} ] millis", request.getMethod(), request.getRequestURI(), after - before);
        return res;
    }
    //@annotation(cn.edu.sdtbu.aop.annotation.Debug)
}
