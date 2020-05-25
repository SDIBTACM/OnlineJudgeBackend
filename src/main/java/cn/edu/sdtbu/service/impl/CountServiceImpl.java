package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.cache.CacheStore;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.entity.CountEntity;
import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.repository.CountRepository;
import cn.edu.sdtbu.repository.SolutionRepository;
import cn.edu.sdtbu.repository.user.UserRepository;
import cn.edu.sdtbu.service.CountService;
import cn.edu.sdtbu.util.CacheUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
    @Resource
    SolutionRepository solutionRepository;
    @Resource
    UserRepository userRepository;

    @Override
    public Map<String, Long> fetchByKeyLike(String key) {
        return repository.findAllByCountKeyLike(key).stream().collect(
            Collectors.toMap(CountEntity::getCountKey, CountEntity::getTotal));
    }
    @Override
    public boolean refreshJudgeResultByUserId(Long userId, boolean needReturnBiggerThanMaxId) {
        List<SolutionEntity> list = solutionRepository.findAllByOwnerId(userId);
        Map<JudgeResult, Long> map = new HashMap<>(JudgeResult.values().length);
        list.forEach(i -> map.put(i.getResult(), 1L + map.getOrDefault(i.getResult(),0L)));

        AtomicReference<Long> total = new AtomicReference<>(0L);
        AtomicReference<Long> accepted = new AtomicReference<>(0L);
        map.forEach((f, r) -> {
            setCount(CacheUtil.judgeResultCountKey(f, userId, false), r);
            if (f.equals(JudgeResult.ACCEPT)) {
                accepted.set(r);
            }
            total.set(total.get() + r);
        });

        log.info("user's solution refresh completed, id is [{}], solution count is: {}", userId, JSON.toJSONString(map));
        return needReturnBiggerThanMaxId && (userId > userRepository.count());
    }

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
        cache().put(key, val.toString(), 3, TimeUnit.DAYS);
        CountEntity entity = repository.findByCountKey(key);
        if (entity == null) {
            entity = new CountEntity();
        }
        entity.setCountKey(key);
        entity.setTotal(val);
        save(entity);
    }

    @Override
    public Map<String, Long> fetchCountByKeys(Collection<String> keys) {
        return repository.findAllByCountKeyIn(keys).stream().collect(
            Collectors.toMap(CountEntity::getCountKey, CountEntity::getTotal));
    }

    private void save(CountEntity entity) {
        repository.saveAndFlush(entity);
    }
    private CacheStore<String, String> cache() {
        return cacheHandler.fetchCacheStore();
    }
}
