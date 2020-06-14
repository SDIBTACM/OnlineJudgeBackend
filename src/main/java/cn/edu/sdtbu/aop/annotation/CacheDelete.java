package cn.edu.sdtbu.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author bestsort
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Documented
public @interface CacheDelete {
    /**
     * key witch need to be delete/refresh, support SpEL
     *
     * @return key
     */
    String deleteKey();

    /**
     * refresh value or not
     *
     * @return is or not
     */
    boolean refresh() default false;

    /**
     * target class, used to fetch prefix
     * specify a concrete class rather than an interface or abstract class
     *
     * @return class
     */
    Class<?> targetClass();

    /**
     * method name
     *
     * @return name
     */
    String method();
}
