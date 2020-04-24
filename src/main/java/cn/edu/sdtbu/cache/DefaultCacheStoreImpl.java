package cn.edu.sdtbu.cache;

import cn.edu.sdtbu.manager.RedisManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 15:56
 */
@Service
public class DefaultCacheStoreImpl extends AbstractCacheStore<String, String> {
    @Resource
    RedisManager cacheManager;
    @Override
    public Optional<String> getInternal(String key) {
        return Optional.ofNullable(cacheManager.get(key));
    }
}