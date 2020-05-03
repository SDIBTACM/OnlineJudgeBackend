package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.CacheEntity;
import cn.edu.sdtbu.service.base.BaseService;

import java.util.Collection;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 21:16
 */
public interface CacheService extends BaseService<CacheEntity, Long> {
    void removeByKey(String key);
    void put(String key, String value);
    String get(String key);
    void inc(String key, int stepLength);
    void removeByKeysIn(Collection<String> collection);
}
