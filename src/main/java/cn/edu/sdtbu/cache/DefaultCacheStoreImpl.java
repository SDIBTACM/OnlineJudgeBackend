package cn.edu.sdtbu.cache;

import cn.edu.sdtbu.model.enums.CacheStoreType;
import cn.edu.sdtbu.service.CacheService;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    public void sortedListAdd(String listName, Map<String, Double> scoreValMap) {
        //TODO default cache rank impl
    }

    @Override
    public void sortedListAdd(String listName, String value, double score) {

    }

    @Override
    public Set<Tuple> fetchRanksByPage(String listName, Pageable pageable, boolean less) {
        return null;
    }

    @Override
    public void putInternal(@NotNull String key, @NotNull String value, long timeout, @NotNull TimeUnit timeUnit) {
        service.put(key, value);
    }

    @Override
    public void inc(@NonNull String key, int stepLength) {
        service.inc(key, stepLength);
    }

    @Override
    public Long totalElementOfList(String key) {
        return 0L;
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

    @Override
    public Long count(String key) {
        return 0L;
    }

    @Override
    public Long ttl(String key) {
        return null;
    }

    @Override
    public Long zRank(String key, String member, boolean less) {
        return null;
    }
}
