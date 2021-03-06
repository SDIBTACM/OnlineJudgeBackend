package cn.edu.sdtbu.repository;

import cn.edu.sdtbu.model.entity.CacheEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-22 21:15
 */
public interface CacheRepository extends BaseRepository<CacheEntity, Long> {
    void removeByKey(String key);

    void removeByKeyIn(Collection<String> keys);

    List<CacheEntity> findAllByKeyLike(String keys);

    Optional<CacheEntity> findByKey(String key);
}
