package cn.edu.sdtbu.repository.user;

import cn.edu.sdtbu.model.entity.user.UserClassEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 17:43
 */
public interface UserClassRepository extends BaseRepository<UserClassEntity, Long> {
    List<UserClassEntity> findAllByClassIdAndDeleteAt(Long classId, Timestamp deleteAt);
    List<UserClassEntity> findAllByClassIdInAndDeleteAt(Collection<Long> classIds, Timestamp deleteAt);
    List<UserClassEntity> findAllByClassIdIn(Collection<Long> ids);
}
