package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.cache.CacheStore;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.entity.CountEntity;
import cn.edu.sdtbu.repository.CountRepository;
import cn.edu.sdtbu.service.CountService;
import cn.edu.sdtbu.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-03 15:01
 */
@Slf4j
@Service
public class CountServiceImpl implements CountService {
    private final CacheHandler cacheHandler;
    private final CountRepository repository;
    public CountServiceImpl(CountRepository repository, CacheHandler cacheHandler) {
        this.repository = repository;
        this.cacheHandler = cacheHandler;
    }
    public void flushCountFromDb() {
        Map<String, String> map = cache().fetchAll(CacheUtil.COUNT_PREFIX + CacheUtil.SEPARATOR);
        //TODO parse
    }

    @Override
    public Long fetchCount(String key) {
        String value;
        if (StringUtils.isEmpty(value = cache().get(key))) {
            CountEntity entity = repository.findByCountKey(key);
            if (entity == null) {
                entity = new CountEntity();
                entity.setCountKey(key);
                entity.setTotal(0L);
                save(entity);
            }
            cache().put(key, "0", 48, TimeUnit.HOURS);
            return 0L;
        }
        return Long.parseLong(value);
    }

    @Override
    public void  incCount(String key, int step) {
        cache().inc(key, step);
        repository.incByStep(key, (long) step);
    }

    @Override
    public void incCount(String key) {
        incCount(key, 1);
    }

    @Override
    public void setCount(String key, Long val) {
        cache().put(key, val.toString(), 1, TimeUnit.DAYS);
        CountEntity entity = repository.findByCountKey(key);
        if (entity == null) {
            entity = new CountEntity();
        }
        entity.setCountKey(key);
        entity.setTotal(val);
        save(entity);
    }
    private void save(CountEntity entity) {
        repository.saveAndFlush(entity);
    }
    private CacheStore<String, String> cache() {
        return cacheHandler.fetchCacheStore();
    }
}
