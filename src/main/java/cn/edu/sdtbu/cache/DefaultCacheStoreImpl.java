package cn.edu.sdtbu.cache;

import cn.edu.sdtbu.model.enums.CacheStoreType;
import cn.edu.sdtbu.service.CacheService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-26 16:18
 */
@Component
public class DefaultCacheStoreImpl extends AbstractCacheStore<String, String>
    implements CacheStore<String, String> {

    @Resource
    CacheService service;
    @Override
    public Optional<String> getInternal(String key) {
        return Optional.ofNullable(service.get(key));
    }

    @Override
    public void putInternal(@NotNull String key, @NotNull String value, long timeout, @NotNull TimeUnit timeUnit) {
        service.put(key, value);
    }

    @Override
    public void inc(@NotNull String key, int stepLength) {
        service.inc(key, stepLength);
    }

    @Override
    public CacheStoreType getCacheType() {
        return CacheStoreType.DEFAULT;
    }

    @Override
    public void delete(String key) {
        service.removeByKey(key);
    }

    @Override
    public void delete(Collection<String> collection) {
        service.removeByKeysIn(collection);
    }

    @Override
    public Map<String, String> fetchAll(String prefix) {
        return service.fetchAllByPrefix(prefix);
    }
}
