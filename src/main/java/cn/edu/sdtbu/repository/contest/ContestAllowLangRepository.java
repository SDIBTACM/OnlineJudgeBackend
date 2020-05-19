package cn.edu.sdtbu.repository.contest;

import cn.edu.sdtbu.model.entity.contest.AllowLangEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 17:14
 */
public interface ContestAllowLangRepository extends BaseRepository<AllowLangEntity, Long> {
    List<AllowLangEntity> findAllByContestIdAndDeleteAt(Long contestId, Timestamp deleteAt);
}
