package cn.edu.sdtbu.repository.contest;

import cn.edu.sdtbu.model.entity.contest.ContestProblemEntity;
import cn.edu.sdtbu.repository.base.BaseRepository;

import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 19:33
 */
public interface ContestProblemRepository extends BaseRepository<ContestProblemEntity, Long> {
    ContestProblemEntity findByContestIdAndProblemOrder(long contestId, int order);

    List<ContestProblemEntity> findAllByContestId(long contestId);
}
