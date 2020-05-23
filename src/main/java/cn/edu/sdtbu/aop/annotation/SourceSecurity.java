package cn.edu.sdtbu.aop.annotation;

import cn.edu.sdtbu.model.enums.SecurityType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-23 16:35
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Documented
public @interface SourceSecurity {
    SecurityType value();
    boolean throwException() default true;
}
