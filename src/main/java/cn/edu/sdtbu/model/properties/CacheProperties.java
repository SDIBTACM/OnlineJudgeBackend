package cn.edu.sdtbu.model.properties;

import cn.edu.sdtbu.model.enums.CacheStoreType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-05 15:31
 */
@Getter
@Setter
public class CacheProperties {
    @NestedConfigurationProperty
    private CacheStoreType cacheStoreType = CacheStoreType.REDIS;

    private Long defaultExpire = 60L;

    private Long defaultNullObjectExpire = 5L;

    @NestedConfigurationProperty
    private TimeUnit defaultExpireTimeUnit = TimeUnit.MINUTES;
}
