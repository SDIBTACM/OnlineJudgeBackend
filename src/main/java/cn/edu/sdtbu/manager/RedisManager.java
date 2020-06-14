package cn.edu.sdtbu.manager;

import org.springframework.data.domain.Pageable;
import redis.clients.jedis.Tuple;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 21:21
 */
public interface RedisManager {
    String get(String key);

    Map<String, String> fetchAll(String prefix);

    Set<Tuple> fetchRanksByPage(String listName, Pageable pageable, boolean less);

    void delete(String key);

    void put(String key, String value, long timeOut, TimeUnit timeUnit);

    void inc(String key, int stepLength);

    void sortedListAdd(String key, Map<String, Double> doubleStringMap);

    void sortedListAdd(String key, String value, double score);

    Long totalElementOfList(String key);

    Long ttl(String key);

    Long zRank(String key, String member, boolean less);
}
