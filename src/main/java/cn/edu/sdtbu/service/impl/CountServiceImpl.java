package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.CountEntity;
import cn.edu.sdtbu.repository.CountRepository;
import cn.edu.sdtbu.service.CountService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.CacheUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-03 15:01
 */
@Service
public class CountServiceImpl extends AbstractBaseService<CountEntity, Long> implements CountService {

    private final CountRepository repository;
    public CountServiceImpl(CountRepository repository) {
        super(repository);
        this.repository = repository;
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
}
