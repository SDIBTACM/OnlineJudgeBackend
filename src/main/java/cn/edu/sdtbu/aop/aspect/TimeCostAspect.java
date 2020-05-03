package cn.edu.sdtbu.aop.aspect;

import cn.edu.sdtbu.model.properties.Const;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
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
    @Around("execution(* cn.edu.sdtbu.controller.api..*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
            RequestContextHolder.getRequestAttributes())).getRequest();
        Object res = joinPoint.proceed();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        Long before = (Long)requestAttributes.getAttribute(Const.REQUEST_PROCESS_BEFORE,0);
        long costTime = -1;
        if (before != null && response != null) {
            costTime = System.nanoTime() - before;
            response.setHeader(Const.REQUEST_COST_TIME, costTime + " ns");
        }
        log.debug("{}: {} was cost [ {} ] nanosecond", request.getMethod(), request.getRequestURI(), costTime);
        return res;
    }
    //@annotation(cn.edu.sdtbu.aop.annotation.Debug)
}
