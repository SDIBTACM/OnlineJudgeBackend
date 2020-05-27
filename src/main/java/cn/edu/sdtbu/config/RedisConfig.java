package cn.edu.sdtbu.config;

import cn.edu.sdtbu.model.properties.RedisProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 22:05
 */
@Slf4j
@Getter
@Setter
@Configuration
public class RedisConfig {
    @Resource
    RedisProperties properties;
    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(properties.getMaxTotal());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMaxWaitMillis(properties.getMaxWaitMillis());
        config.setMinIdle(properties.getMinIdle());
        if (StringUtils.isEmpty(properties.getPassword())) {
            properties.setPassword(null);
        }
        JedisPool pool = new JedisPool(
            config,
            properties.getHost(),
            properties.getPort(),
            properties.getMaxWaitMillis(),
            properties.getPassword());
        log.info("redis pool build success");
        log.info("redis start success, redis host: {}", AnsiOutput.toString(
            AnsiColor.BLUE,
            properties.getHost(),
            ":",
            properties.getPort()));
        return pool;
    }
}
