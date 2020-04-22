package cn.edu.sdtbu.cache;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 14:52
 */
public interface CacheStore<K, V> {

    /**
     * Gets by cache key.
     * @param key must not be null
     * @return cache value
     */
    V get(K key);

    /**
     * get val or return default value when val is null
     * @param key key
     * @param defaultValue default
     * @return  value
     */
    V getOrElse(K key, V defaultValue);
    /**
     * get val or throw a exception when val is null
     * @param key key
     * @param exceptionSupplier exception
     * @return value
     */
    <X extends Throwable> X getOrElseThrow(K key, Supplier<? extends X> exceptionSupplier) throws Throwable;

    /**
     * put a cache with will be expired, and do not backup for this k-v
     * @param key      cache key must not be null
     * @param value    cache value must not be null
     * @param timeout  the key expiration must not be less than 1
     * @param timeUnit timeout unit
     */
    void putWithoutBackup(@NotNull K key, @NotNull V value, long timeout, @NotNull TimeUnit timeUnit);

    /**
     * Puts a cache which will be expired.
     * @param key      cache key must not be null
     * @param value    cache value must not be null
     * @param timeout  the key expiration must not be less than 1
     * @param timeUnit timeout unit
     */
    void put(@NonNull K key, @NonNull V value, long timeout, @NonNull TimeUnit timeUnit);

    /**
     * Puts a non-expired cache.
     *
     * @param key   cache key must not be null
     * @param value cache value must not be null
     */
    void put(K key, V value);

    /**
     * add by step length
     * @param key   cache key must not be null
     * @param stepLength  cache value must not be null
     */
    void inc(@NotNull K key, int stepLength);
}
