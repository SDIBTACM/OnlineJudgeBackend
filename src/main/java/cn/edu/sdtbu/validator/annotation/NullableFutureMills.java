package cn.edu.sdtbu.validator.annotation;

import cn.edu.sdtbu.validator.NullableFutureMillsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-20 09:05
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NullableFutureMillsValidator.class)
public @interface NullableFutureMills {
    String message() default "{javax.validation.constraints.Future.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}