package cn.edu.sdtbu.handler;

import cn.edu.sdtbu.cache.CacheStore;
import cn.edu.sdtbu.model.enums.CacheStoreType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-26 16:30
 */
@Slf4j
@Component
public class CacheHandler {
    private final ConcurrentHashMap<CacheStoreType, CacheStore<String, String>> cacheMap = new ConcurrentHashMap<>();
    private static CacheStoreType STRATEGY = CacheStoreType.DEFAULT;

    public void setStrategy(CacheStoreType strategy) {
        setStrategy(strategy, strategy != STRATEGY);
    }

    private void setStrategy(CacheStoreType strategy, boolean isChanged) {
        Assert.notNull(strategy, "storage must be not null");
        if (isChanged) {
            log.info("cache middleware was changed to {}", strategy);
            STRATEGY = strategy;
        }
    }

    public String getStrategy() {
        return STRATEGY.toString();
    }

    public CacheStore<String, String> fetchCacheStore() {
        CacheStore<String, String> store = cacheMap.get(STRATEGY);
        Assert.notNull(store,"cache storage not found");
        return  store;
    }

    public CacheHandler(ApplicationContext applicationContext) {
        addCacheHandlers(applicationContext.getBeansOfType(CacheStore.class).values());
    }
    private void addCacheHandlers(@Nullable Collection<CacheStore> cacheHandlers) {
        if (!CollectionUtils.isEmpty(cacheHandlers)) {
            for (CacheStore cacheHandler : cacheHandlers) {
                this.cacheMap.put(cacheHandler.getCacheType(), cacheHandler);
            }
        }
    }
}
