package cn.edu.sdtbu.config;

import cn.edu.sdtbu.aop.aspect.TimeCostAspect;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-12 10:45
 */
public class TimeCostConfig implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{TimeCostAspect.class.getCanonicalName()};
    }
}
