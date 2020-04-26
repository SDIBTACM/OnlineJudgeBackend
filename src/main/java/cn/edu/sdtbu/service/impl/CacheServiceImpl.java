package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.CacheEntity;
import cn.edu.sdtbu.repository.CacheRepository;
import cn.edu.sdtbu.service.CacheService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-24 13:22
 */
@Service
public class CacheServiceImpl extends AbstractBaseService<CacheEntity, Long> implements CacheService {
    protected CacheServiceImpl(CacheRepository repository) {
        super(repository);
    }
    @Resource
    CacheRepository cacheRepository;
    @Override
    public void removeByKey(String key) {
        cacheRepository.removeByKey(key);
    }

    @Override
    public void put(String key, String value) {
        CacheEntity entity = cacheRepository.findByKey(key).orElse(new CacheEntity());
        if (StringUtils.isEmpty(entity.getKey())) {
            entity.setKey(key);
        }
        if (StringUtils.equals(entity.getValue(), value)) {
            return;
        }
        entity.setValue(value);
        cacheRepository.saveAndFlush(entity);
    }

    @Override
    public String get(String key) {
        CacheEntity entity = cacheRepository.findByKey(key).orElse(null);
        return entity == null ? null : entity.getValue();
    }

    @Override
    public void inc(String key, int stepLength) {
        CacheEntity entity = cacheRepository.findByKey(key).orElse(new CacheEntity());
        if (StringUtils.isEmpty(entity.getKey())) {
            entity.setKey(key);
            entity.setValue("0");
        }
        entity.setValue((Long.parseLong(entity.getValue()) + stepLength) + "");
        cacheRepository.saveAndFlush(entity);
    }
}
