package cn.edu.sdtbu.manager;

import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 21:21
 */
public interface RedisManager {
    String get(String key);
    void inc(String key, int stepLength);
    void put(String key, String value, long timeOut, TimeUnit timeUnit);

    void delete(String key);
}
