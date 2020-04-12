package cn.edu.sdtbu.aop.annotation;

import cn.edu.sdtbu.config.TimeCostConfig;
import cn.edu.sdtbu.model.properties.DebugProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bestsort
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DebugProperties.class, TimeCostConfig.class})
public @interface ShowTimeCost {
}
