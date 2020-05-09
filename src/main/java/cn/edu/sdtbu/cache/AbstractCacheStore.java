package cn.edu.sdtbu.cache;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 15:59
 */
public abstract class AbstractCacheStore<K, V> implements CacheStore<K, V> {
    /**
     * default expire : 60 minutes
     */
    static final long DEFAULT_EXPIRE = 60;
    //@Resource
    //ApplicationEventPublisher applicationEventPublisher;

    @Override
    public V get(K key) {
        return getInternal(key).orElse(null);
    }

    @Override
    public V getOrElse(K key, V defaultValue) {
        Assert.notNull(key, "Cache key must not be blank");
        return getInternal(key).orElse(defaultValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X extends Throwable> X getOrElseThrow(K key, Supplier<? extends X> exceptionSupplier) throws X {
        return (X) getInternal(key).orElseThrow(exceptionSupplier);
    }

    @Override
    public void put(@NonNull K key,@NonNull V value, long timeout,@NonNull TimeUnit timeUnit) {
        putInternal(key, value, timeout, timeUnit);
    }

    /**
     * get value
     * @param key key
     * @return value in cache
     */
    public abstract Optional<V> getInternal(K key);

    @Override
    public void put(K key, V value) {
        V oldValue = get(key);
        if (oldValue != null && oldValue.equals(value)) {
            return;
        }
        // put to db
        // put to cache middleware
        putInternal(key, value, DEFAULT_EXPIRE, TimeUnit.MINUTES);
    }

    @Override
    public void put(Map<K, V> kvMap) {
        for (K k : kvMap.keySet()) {
            put(k, kvMap.get(k));
        }
    }
}
