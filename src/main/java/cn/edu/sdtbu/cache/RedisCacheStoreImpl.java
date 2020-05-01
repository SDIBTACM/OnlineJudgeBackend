package cn.edu.sdtbu.cache;

import cn.edu.sdtbu.manager.RedisManager;
import cn.edu.sdtbu.model.enums.CacheStoreType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 15:56
 */
@Service
public class RedisCacheStoreImpl extends AbstractCacheStore<String, String> implements CacheStore<String, String> {
    @Resource
    RedisManager manager;

    @Override
    public Optional<String> getInternal(String key) {
        return Optional.ofNullable(manager.get(key));
    }

    @Override
    public void putInternal(@NotNull String key, @NotNull String value, long timeout, @NotNull TimeUnit timeUnit) {
        manager.put(key, value, timeout, timeUnit);
    }

    @Override
    public void inc(@NotNull String key, int stepLength) {
        manager.inc(key, stepLength);
    }

    @Override
    public CacheStoreType getCacheType() {
        return CacheStoreType.REDIS;
    }

    @Override
    public void delete(String key) {
        manager.delete(key);
    }
}