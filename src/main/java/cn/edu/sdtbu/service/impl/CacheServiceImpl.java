package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.CacheEntity;
import cn.edu.sdtbu.repository.CacheRepository;
import cn.edu.sdtbu.service.CacheService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;

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

    @Override
    public void removeByKey(String toString) {

    }

    @Override
    public void put(String key, String value) {

    }

    @Override
    public void inc(String toString, int stepLength) {

    }
}
