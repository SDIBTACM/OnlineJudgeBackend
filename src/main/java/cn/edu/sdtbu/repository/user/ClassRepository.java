package cn.edu.sdtbu.repository.user;

import cn.edu.sdtbu.model.entity.user.ClassEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-21 15:13
 */
public interface ClassRepository extends BaseRepository<ClassEntity, Long> {
    List<ClassEntity> findAllByOwnerIdAndDeleteAt(Long ownerId, Timestamp deleteAt);
}
