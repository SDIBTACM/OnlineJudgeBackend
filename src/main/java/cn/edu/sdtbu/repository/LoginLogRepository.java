package cn.edu.sdtbu.repository;

import cn.edu.sdtbu.model.entity.LoginLogEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 10:41
 */
public interface LoginLogRepository extends BaseRepository<LoginLogEntity, Long> {
    /**
     * find all login log
     * @param userId    id
     * @param pageable  paging rules
     * @return  result
     */
    Page<LoginLogEntity> findAllByUserId(@NonNull Long userId, @NonNull Pageable pageable);
}