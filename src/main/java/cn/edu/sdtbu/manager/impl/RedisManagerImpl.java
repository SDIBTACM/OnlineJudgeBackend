package cn.edu.sdtbu.manager.impl;

import cn.edu.sdtbu.manager.RedisManager;
import cn.edu.sdtbu.util.TimeUtil;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 22:04
 */
@Component
public class RedisManagerImpl implements RedisManager {
    @Resource
    private JedisPool pool;

    @Override
    public String get(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }

    @Override
    public void inc(String key, int stepLength) {
        try (Jedis jedis = pool.getResource()) {
            jedis.incrBy(key, stepLength);
        }
    }

    @Override
    public void put(String key, String value, long timeOut, TimeUnit timeUnit) {
        try (Jedis jedis = pool.getResource()) {
            if (jedis.exists(key)){
                jedis.del(key);
            }
            jedis.set(key, value, "nx", "px", TimeUtil.time2Mill(timeOut, timeUnit));
        }
    }
}
