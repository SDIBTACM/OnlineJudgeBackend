package cn.edu.sdtbu.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-03 15:00
 */

public interface CountService {
    Map<String, Long> fetchByKeyLike(String key);

    boolean refreshJudgeResultByUserId(Long userId, boolean needReturnBiggerThanMaxId);

    /**
     * fetch total count by key, if not found, return 0
     *
     * @param key must be not null
     * @return total
     */
    Long fetchCount(String key);

    @Async
    @Transactional(rollbackFor = Exception.class)
    void incCount(String key, int step);

    @Async
    @Transactional(rollbackFor = Exception.class)
    void incCount(String key);

    @Async
    void setCount(String key, Long val);

    Map<String, Long> fetchCountByKeys(Collection<String> keys);
}
