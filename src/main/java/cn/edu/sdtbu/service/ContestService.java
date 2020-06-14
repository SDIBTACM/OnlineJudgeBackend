package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.contest.ContestEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.ContestParam;
import cn.edu.sdtbu.model.vo.ProblemDescVO;
import cn.edu.sdtbu.model.vo.contest.ContestDetailVO;
import cn.edu.sdtbu.model.vo.contest.ContestsVO;
import cn.edu.sdtbu.model.vo.contest.StandingNodeVO;
import cn.edu.sdtbu.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-18 07:36
 */
public interface ContestService extends BaseService<ContestEntity, Long> {
    Page<ContestsVO> fetchContests(UserEntity userEntity, Pageable page);

    ContestDetailVO fetchDetailContestInfo(long contestId, Long userId);

    /**
     * create contest and generator other info about contest
     *
     * @param param contest param
     * @param user  at least the {@link cn.edu.sdtbu.model.enums.UserRole#TEACHER}
     */
    @Transactional
    void createContest(ContestParam param, UserEntity user);

    ProblemDescVO getContestProblemDesc(long contest, int order, Long userId);

    Page<StandingNodeVO> getStandings(long contestId);
}
