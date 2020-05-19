package cn.edu.sdtbu.repository.contest;

import cn.edu.sdtbu.model.entity.contest.ContestPrivilegeEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 17:02
 */
public interface ContestPrivilegeRepository extends BaseRepository<ContestPrivilegeEntity, Long> {
    List<ContestPrivilegeEntity> findAllByContestIdAndDeleteAt(Long contestId, Timestamp deleteAt);
}
