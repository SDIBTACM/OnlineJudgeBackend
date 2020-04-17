package cn.edu.sdtbu.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-12 08:35
 */
@Slf4j
@Aspect
@Component
public class TimeCostAspect {

    @Around("execution(* cn.edu.sdtbu.controller.api.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
            RequestContextHolder.getRequestAttributes())).getRequest();

        long before = System.nanoTime();
        Object res = joinPoint.proceed();
        long costTime = System.nanoTime() - before;

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.setHeader("Request-Cost-Time", costTime + " ns");
        }

        log.debug("{}: {} was cost [ {} ] nanosecond", request.getMethod(), request.getRequestURI(), costTime);
        return res;
    }
    //@annotation(cn.edu.sdtbu.aop.annotation.Debug)
}
