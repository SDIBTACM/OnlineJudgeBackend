package cn.edu.sdtbu.aop.aspect;

import cn.edu.sdtbu.aop.annotation.SourceSecurity;
import cn.edu.sdtbu.exception.UnauthorizedException;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.SecurityType;
import cn.edu.sdtbu.model.properties.Const;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-23 16:40
 */

@Slf4j
@Aspect
@Component
public class SecurityAspect {
    private final HttpSession session;

    public SecurityAspect(HttpSession session) {
        this.session = session;
    }

    @Around("@annotation(cn.edu.sdtbu.aop.annotation.SourceSecurity)")
    public Object before(ProceedingJoinPoint point) throws Throwable {
        SourceSecurity security = fetchAnnotation(point, SourceSecurity.class);
        UserEntity entity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
        SecurityType type = security.value();
        if (!type.equals(SecurityType.NONE)) {
            if (entity == null || type.getValue() > entity.getRole().getValue()) {
                if (security.throwException()) {
                    throw new UnauthorizedException(
                        String.format("unauthorized, need [%s], your role is [%s]", type,
                            entity == null ? "UNLOGIN" : entity.getRole()));
                }
                return ResponseEntity.ok(ResponseEntity.noContent());
            }
        }
        return point.proceed();
    }
    private <T extends Annotation> T fetchAnnotation(JoinPoint point, Class<T> annotationClass) {
        return ((MethodSignature)point.getSignature())
            .getMethod()
            .getAnnotation(annotationClass);
    }
}
