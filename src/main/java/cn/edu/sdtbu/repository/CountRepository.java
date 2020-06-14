package cn.edu.sdtbu.repository;

import cn.edu.sdtbu.model.entity.CountEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-03 14:56
 */
public interface CountRepository extends BaseRepository<CountEntity, Long> {
    /**
     * fetch by count key
     *
     * @param countKey must be not null
     * @return res
     */
    CountEntity findByCountKey(String countKey);

    /**
     * inc by step.
     *
     * @param key  key
     * @param step must not be null
     */
    @Transactional
    @Modifying
    @Query("update CountEntity set total = total + :step where countKey = :count_key")
    void incByStep(@Param("count_key") String key, @Param("step") Long step);

    List<CountEntity> findAllByCountKeyIn(Collection<String> keys);

    List<CountEntity> findAllByCountKeyLike(String keyPrefix);
}
