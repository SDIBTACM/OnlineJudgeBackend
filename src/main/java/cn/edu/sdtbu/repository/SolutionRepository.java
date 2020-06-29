package cn.edu.sdtbu.repository;

import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-21 15:40
 */
public interface SolutionRepository extends BaseRepository<SolutionEntity, Long> {
    List<SolutionEntity> findAllByOwnerId(Long ownerId);

    List<SolutionEntity> findAllByProblemId(Long problemId);

    List<SolutionEntity> findAllByContestId(long contestId);

    List<SolutionEntity> findAllByContestIdAndProblemId(long contestId, long problemId);

    List<SolutionEntity> findAllByOwnerIdAndResultAndProblemIdIn(Long userId, JudgeResult result, Collection<Long> problemIds);

    Boolean existsByOwnerIdAndProblemIdAndContestIdAndResult(Long userId, Long problemId, Long contestId, JudgeResult result);

    Boolean existsByOwnerIdAndProblemIdAndResult(Long userId, Long problemId, JudgeResult result);

    Page<SolutionEntity> findAllByCreateAtBetween(Timestamp start, Timestamp end, Pageable page);
}
