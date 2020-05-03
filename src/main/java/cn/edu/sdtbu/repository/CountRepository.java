package cn.edu.sdtbu.repository;

import cn.edu.sdtbu.model.entity.CountEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-03 14:56
 */
public interface CountRepository extends BaseRepository<CountEntity, Long> {
    /**
     * fetch by count key
     * @param countKey must be not null
     * @return res
     */
    CountEntity findByCountKey(String countKey);
}
