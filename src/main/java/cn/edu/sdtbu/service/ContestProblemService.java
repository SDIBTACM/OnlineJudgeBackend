package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.contest.ContestProblemEntity;
import cn.edu.sdtbu.service.base.BaseService;

import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-05 15:16
 */
public interface ContestProblemService extends BaseService<ContestProblemEntity, Long> {
    ContestProblemEntity getContestProblem(long contestId, int order);
    List<ContestProblemEntity> listAllContestProblems(long contestId);
}
