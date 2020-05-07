package cn.edu.sdtbu.aop.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * default k-v cache
 * @author bestsort
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Documented
public @interface Cache {
    /**
     * default expire
     * @return 30 minutes
     */
    long expire() default 30;

    /**
     * time unit
     * @return default
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * add or subtract random number on {@link #expire()} to prevent cache avalanche
     * @return default true
     */
    boolean random() default true;

    /**
     * random interval, just valid with {@link #random()} is true
     * @return default
     */
    int randomInterval() default 3;

    /**
     * support spring expression language
     * {@see <a href="https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/expressions.html">SpEL</a>}
     * @return key
     */
    @AliasFor("value")
    String key() default "";

    @AliasFor("key")
    String value() default "";
}
