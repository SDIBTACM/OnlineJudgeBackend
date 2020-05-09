package cn.edu.sdtbu.cache;

import cn.edu.sdtbu.model.enums.CacheStoreType;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
     * add objects to sorted list, if some exist, update them's score
     * @param listName name
     * @param scoreValMap some need to add or update
     */
    @Async
    void sortedListAdd(String listName, Map<String, Double> scoreValMap);

    /**
     * add an object to sorted list, if exist, update it's score
     * @param listName list's name, must be not null
     * @param value object's JSON value
     * @param score score, used to sort compare
     */
    @Async
    void sortedListAdd(String listName, String value, double score);

    /**
     * fetch rank list by page
     * @param listName must be not null
     * @param pageable page
     * @param lesserFirst lesser first if {true} or greater first
     * @return JSON string list
     */
    Collection<V> fetchRanksByPage(String listName, Pageable pageable, boolean lesserFirst);

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
    void putInternal(@NotNull K key, @NotNull V value, long timeout, @NotNull TimeUnit timeUnit);

    /**
     * Puts a cache which will be expired.
     * @param key      cache key must not be null
     * @param value    cache value must not be null
     * @param timeout  the key expiration must not be less than 1
     * @param timeUnit timeout unit
     */
    @Async
    void put(@NonNull K key, @NonNull V value, long timeout, @NonNull TimeUnit timeUnit);

    /**
     * Puts a non-expired cache.
     *
     * @param key   cache key must not be null
     * @param value cache value must not be null
     */
    @Async
    void put(K key, V value);

    void put(Map<K, V> kvMap);
    /**
     * add by step length
     * @param key   cache key must not be null
     * @param stepLength  cache value must not be null
     */
    @Async
    void inc(@NonNull K key, int stepLength);

    Long totalElementOfList(@NonNull String key);
    /**
     * get cache interface impl support's cache type
     * @return type
     */
    CacheStoreType getCacheType();

    void delete(K key);

    void delete(Collection<K> collection);

    Map<K, V> fetchAll(String prefix);
}
