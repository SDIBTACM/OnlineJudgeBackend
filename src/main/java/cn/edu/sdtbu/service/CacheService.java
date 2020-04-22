package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.CacheEntity;
import cn.edu.sdtbu.service.base.BaseService;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 21:16
 */
public interface CacheService extends BaseService<CacheEntity, Long> {
    void removeByKey(String toString);
    void put(String key, String value);

    void inc(String toString, int stepLength);
}
