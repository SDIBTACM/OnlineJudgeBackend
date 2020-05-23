package cn.edu.sdtbu.repository;

import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.repository.base.BaseRepository;

import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-21 15:40
 */
public interface SolutionRepository extends BaseRepository<SolutionEntity, Long> {
    List<SolutionEntity> findAllByOwnerId(Long ownerId);
    List<SolutionEntity> findAllByProblemId(Long problemId);
    Boolean existsByOwnerIdAndProblemIdAndContestIdAndResult(Long userId, Long problemId, Long contestId, JudgeResult result);
    Boolean existsByOwnerIdAndProblemIdAndResult(Long userId, Long problemId, JudgeResult result);
}
