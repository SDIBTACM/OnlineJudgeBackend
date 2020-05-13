package cn.edu.sdtbu.cache;

import cn.edu.sdtbu.manager.RedisManager;
import cn.edu.sdtbu.model.enums.CacheStoreType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 15:56
 */
@Slf4j
@Service
public class RedisCacheStoreImpl extends AbstractCacheStore<String, String> implements CacheStore<String, String> {
    @Resource
    RedisManager manager;

    @Override
    public Optional<String> getInternal(String key) {
        return Optional.ofNullable(manager.get(key));
    }

    @Override
    public void sortedListAdd(String listName, Map<String, Double> scoreValMap) {
        manager.sortedListAdd(listName, scoreValMap);
    }

    @Override
    public void sortedListAdd(String listName, String value, double score) {
        manager.sortedListAdd(listName, value, score);
    }

    @Override
    public Collection<String> fetchRanksByPage(String listName, Pageable pageable, boolean less) {
        return manager.fetchRanksByPage(listName, pageable, less);
    }

    @Override
    public void putInternal(@NotNull String key, @NotNull String value, long timeout, @NotNull TimeUnit timeUnit) {
        manager.put(key, value, timeout, timeUnit);
    }

    @Override
    public void inc(@NonNull String key, int stepLength) {
        log.debug("inc : [ {} ],step is {}", key, stepLength);
        manager.inc(key, stepLength);
    }

    @Override
    public Long totalElementOfList(String key) {
        return manager.totalElementOfList(key);
    }

    @Override
    public CacheStoreType getCacheType() {
        return CacheStoreType.REDIS;
    }

    @Override
    public void delete(String key) {
        manager.delete(key);
    }

    @Override
    public void delete(Collection<String> collection) {
        for (String s : collection) {
            delete(s);
        }
    }

    @Override
    public Map<String, String> fetchAll(String prefix) {
        return manager.fetchAll(prefix);
    }

    @Override
    public long count(String key) {
        return manager.totalElementOfList(key);
    }
}