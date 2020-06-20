package cn.edu.sdtbu.cache;

import cn.edu.sdtbu.model.enums.CacheStoreType;
import com.google.common.collect.Maps;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Tuple;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-26 16:18
 */
@Component
public class DefaultCacheStoreImpl extends AbstractCacheStore<String, String>
    implements CacheStore<String, String> {

    public static ConcurrentHashMap<String, String>                      KV_CACHE_POOL;
    public static ConcurrentSkipListMap<String, TreeMap<String, Double>> SORTED_CACHE_POOL;
    @Override
    public Optional<String> getInternal(String key) {
        return Optional.ofNullable(KV_CACHE_POOL.get(key));
    }

    @Override
    public void sortedListAdd(String listName, Map<String, Double> scoreValMap) {
        TreeMap<String, Double> mp = fetchSortedMp(listName);
        scoreValMap.forEach((f, r) -> mp.put(f, mp.getOrDefault(f, 0.0) + r));
    }

    @Override
    public void sortedListAdd(String listName, String value, double score) {
        TreeMap<String, Double> mp = fetchSortedMp(listName);
        mp.put(value, mp.getOrDefault(value, 0.0) + score);
    }

    @Override
    public Set<Tuple> fetchRanksByPage(@NonNull String listName, Pageable pageable, boolean less) {
        TreeMap<String, Double> mp = fetchSortedMp(listName);
        List<Map.Entry<String, Double>> lst = new ArrayList<>(mp.entrySet());
        lst.sort((o1, o2) -> {
            if (o1.getValue().equals(o2.getValue())) {
                return (less ? 1 : -1) * o1.getKey().compareTo(o2.getKey());
            }
            return (less ? 1 : -1) * o1.getValue().compareTo(o2.getValue());
        });

        int left = Math.max(0, pageable.getPageSize() * pageable.getPageNumber());
        int right = Math.min(lst.size(), (pageable.getPageNumber() + 1) * pageable.getPageSize());
        lst = lst.subList(left, right);

        return new LinkedHashSet<>(lst.stream().map(i -> new Tuple(i.getKey(), i.getValue())).collect(Collectors.toList()));
    }

    @Override
    public void putInternal(@NotNull String key, @NotNull String value, long timeout, @NotNull TimeUnit timeUnit) {
        KV_CACHE_POOL.put(key, value);
    }

    @Override
    public void inc(@NonNull String key, int stepLength) {
        int cnt = Integer.parseInt(KV_CACHE_POOL.getOrDefault(key, "0"));
        cnt = cnt + stepLength;
        KV_CACHE_POOL.put(key, Integer.toString(cnt));
    }

    @Override
    public Long totalElementOfList(@NonNull String key) {
        return (long) fetchSortedMp(key).size();
    }

    @Override
    public CacheStoreType getCacheType() {
        return CacheStoreType.MEMORY;
    }

    @Override
    public void delete(String key) {
        KV_CACHE_POOL.remove(key);
        SORTED_CACHE_POOL.remove(key);
    }

    @Override
    public void delete(Collection<String> collection) {
        collection.forEach(this::delete);
    }

    @Override
    public Map<String, String> fetchAll(String prefix) {
        Map<String, String> res = new HashMap<>();
        KV_CACHE_POOL.forEach((f, r) -> {
            if (f.startsWith(prefix)) {
                res.put(f, r);
            }
        });
        return res;
    }

    @Override
    public Long ttl(String key) {
        return null;
    }

    @Override
    public Long zRank(String key, String member, boolean less) {
        TreeMap<String, Double> mp = fetchSortedMp(key);
        Iterator<Map.Entry<String, Double>> iterator;
        if (less) {
            iterator = mp.descendingMap().entrySet().iterator();
        } else {
            iterator = mp.entrySet().iterator();
        }
        long rank = 1;
        while (iterator.hasNext()) {
            if (iterator.next().getKey().equals(member)) {
                return rank;
            }
            rank++;
        }
        return 0L;
    }

    @Override
    public void clearCachePool() {
        KV_CACHE_POOL = null;
        SORTED_CACHE_POOL = null;
    }

    @Override
    public void init() {
        KV_CACHE_POOL = new ConcurrentHashMap<>();
        SORTED_CACHE_POOL = new ConcurrentSkipListMap<>();
        super.init();
    }

    private TreeMap<String, Double> fetchSortedMp(@NonNull String key) {
        TreeMap<String, Double> mp = SORTED_CACHE_POOL.get(key);
        boolean isInit = mp == null;
        if (isInit) {
            mp = Maps.newTreeMap();
            SORTED_CACHE_POOL.put(key, mp);
            return mp;
        }
        return mp;
    }
}
