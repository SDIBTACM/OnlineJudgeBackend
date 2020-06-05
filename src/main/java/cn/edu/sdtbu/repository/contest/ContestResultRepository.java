package cn.edu.sdtbu.repository.contest;

import cn.edu.sdtbu.model.entity.contest.ContestResultEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;

import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-05 20:20
 */
public interface ContestResultRepository extends BaseRepository<ContestResultEntity, Long> {
    List<ContestResultEntity> findAllByContestId(long contestId);

}
