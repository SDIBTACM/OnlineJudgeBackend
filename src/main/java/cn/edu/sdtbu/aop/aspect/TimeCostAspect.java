package cn.edu.sdtbu.aop.aspect;

import cn.edu.sdtbu.model.properties.Const;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-12 08:35
 */
@Slf4j
@Aspect
@Component
public class TimeCostAspect {
    @After("execution(* cn.edu.sdtbu.controller.api..*.*(..))")
    public void around() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        Long before = (Long)requestAttributes.getAttribute(Const.REQUEST_START_TIMESTAMP,0);
        if (before != null && response != null) {
            long costTime = System.nanoTime() - before;
            response.setHeader(Const.REQUEST_COST_TIME, String.format("%.3f ms", 1.0 * costTime / 1e6));
        }
    }
    //@annotation(cn.edu.sdtbu.aop.annotation.Debug)
}
