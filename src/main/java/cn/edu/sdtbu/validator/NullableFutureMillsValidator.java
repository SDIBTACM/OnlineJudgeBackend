package cn.edu.sdtbu.validator;

import cn.edu.sdtbu.util.TimeUtil;
import cn.edu.sdtbu.validator.annotation.NullableFutureMills;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-20 09:05
 */
public class NullableFutureMillsValidator implements ConstraintValidator<NullableFutureMills, Long> {
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value == null || TimeUtil.now().before(new Timestamp(value));
    }
}
