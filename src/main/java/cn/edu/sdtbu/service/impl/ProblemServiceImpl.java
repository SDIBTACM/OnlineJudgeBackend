package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.exception.UnauthorizedException;
import cn.edu.sdtbu.model.entity.contest.ContestEntity;
import cn.edu.sdtbu.model.entity.contest.ContestPrivilegeEntity;
import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.*;
import cn.edu.sdtbu.model.param.ProblemParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.ProblemDescVO;
import cn.edu.sdtbu.model.vo.ProblemSimpleListVO;
import cn.edu.sdtbu.model.vo.user.UserCenterVO;
import cn.edu.sdtbu.repository.ProblemDescRepository;
import cn.edu.sdtbu.repository.ProblemRepository;
import cn.edu.sdtbu.repository.SolutionRepository;
import cn.edu.sdtbu.repository.contest.ContestPrivilegeRepository;
import cn.edu.sdtbu.service.ContestService;
import cn.edu.sdtbu.service.ProblemService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.CacheUtil;
import cn.edu.sdtbu.util.SpringUtil;
import cn.edu.sdtbu.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-14 18:39
 */

@Slf4j
@Service
public class ProblemServiceImpl extends AbstractBaseService<ProblemEntity, Long> implements ProblemService {

    @Override
    public Page<ProblemSimpleListVO> listSimpleLists(Pageable pageable) {
        Page<ProblemEntity> problemEntities = listAll(pageable);
        List<ProblemSimpleListVO> listVOList = new LinkedList<>();
        for (ProblemEntity entity : problemEntities.getContent()) {
            ProblemSimpleListVO buffer = new ProblemSimpleListVO();
            SpringUtil.cloneWithoutNullVal(entity, buffer);
            buffer.setProblemId(entity.getId());
            //TODO real data
            buffer.setAcCount(0L);
            buffer.setSubmitCount(0L);
            buffer.setIsAccepted(System.currentTimeMillis() % 3 == 1);
            buffer.setLastSubmit(TimeUtil.now());
            buffer.setSubmitPeopleCount(0L);
            listVOList.add(buffer);
        }
        return new PageImpl<>(listVOList, pageable, problemEntities.getTotalElements());
    }

    @Override
    public UserCenterVO fetchAllUserSubmitStatus(Long userId) {
        List<SolutionEntity> list = solutionRepository.findAllByOwnerId(userId);
        List<Long> accepted = new LinkedList<>();
        List<Long> unsolved = new LinkedList<>();
        Map<JudgeResult, Long> resultMap = new TreeMap<>();

        HashMap<Long, JudgeResult> status = new HashMap<>();
        list.forEach(item -> {
            JudgeResult buffer;
            resultMap.put(item.getResult(), resultMap.getOrDefault(item.getResult(), 0L) + 1);
            if ((buffer = status.get(item.getProblemId())) == null || buffer != JudgeResult.ACCEPT) {
                status.put(item.getProblemId(), item.getResult());
            }
        });
        status.forEach((k,v) -> {
            if (v == JudgeResult.ACCEPT) {
                accepted.add(k);
            } else {
                unsolved.add(k);
            }
        });
        // cache common fields
        countService.setCount(
            CacheUtil.defaultKey(UserEntity.class, userId, KeyPrefix.USER_ACCEPTED_COUNT),
            (long) accepted.size());
        countService.setCount(
            CacheUtil.defaultKey(UserEntity.class, userId, KeyPrefix.USER_SUBMIT_COUNT),
            (long) list.size()
        );
        return new UserCenterVO(accepted, unsolved, resultMap);
    }
    @Override
    public void generatorProblem(ProblemParam param) {
        create(param.transformToEntity());
        descRepository.saveAndFlush(param.transFormToDescEntity());
    }

    @Override
    public ProblemDescVO getProblemDescVoById(ProblemDescVO vo, Long id, Long contestId, Long userId) {
        checkPrivilege(userId,id, contestId);
        vo = getCount(vo, id, contestId);
        if (userId == null) {
            vo.setIsAccepted(false);
        } else {
            Boolean accepted = contestId == null ?
                solutionRepository.existsByOwnerIdAndProblemIdAndResult(userId, id, JudgeResult.ACCEPT) :
                solutionRepository.existsByOwnerIdAndProblemIdAndContestIdAndResult(userId, id, contestId, JudgeResult.ACCEPT);

            vo.setIsAccepted(accepted == null ? false : accepted);
        }
        SpringUtil.cloneWithoutNullVal(getById(id), vo);
        return vo;
    }

    @Override
    public void refreshSolutionCount(Long problemId) {
        List<SolutionEntity> solutions;
        if (problemId == null) {
            solutions = solutionRepository.findAll();
        } else {
            solutions = solutionRepository.findAllByProblemId(problemId);
        }
        Map<Long, Long> accepted = new HashMap<>();
        Map<Long, Long> submitted = new HashMap<>();

        solutions.forEach(i -> {
            if (i.getResult().equals(JudgeResult.ACCEPT)) {
                accepted.put(i.getProblemId(), accepted.getOrDefault(i.getProblemId(), 0L) + 1);
            }
            submitted.put(i.getProblemId(), submitted.getOrDefault(i.getProblemId(), 0L) + 1);
        });
        submitted.forEach((f,s) -> {
            countService.setCount(CacheUtil.defaultKey(
                ProblemEntity.class, f, KeyPrefix.PROBLEM_TOTAL_ACCEPT), accepted.getOrDefault(f, 0L));
            countService.setCount(CacheUtil.defaultKey(
                ProblemEntity.class, f, KeyPrefix.PROBLEM_TOTAL_SUBMIT), s);

        });
        log.info("problem solution count refresh completed, problem id is {}",
            problemId == null ? "all" : problemId);
    }

    private void checkPrivilege(Long userId,Long problemId, Long contestId) {
        List<Pair<Class<?>, String>> list = new LinkedList<>();
        list.add(CacheUtil.pair(UserEntity.class, userId));
        list.add(CacheUtil.pair(ProblemEntity.class, problemId));
        list.add(CacheUtil.pair(ContestEntity.class, contestId));

        String key = CacheUtil.defaultKey(list, KeyPrefix.CONTEST_PRIVILEGE);

        if (Boolean.parseBoolean(cache().get(key))) {
            return;
        }
        cache().put(key, Boolean.FALSE.toString());
        //TODO problem normal privilege
        checkContestPrivilege(userId, contestId);
        cache().put(key, Boolean.TRUE.toString());
    }

    private void checkContestPrivilege(Long userId, Long contestId) {
        boolean checkPrivilegePassed = true;
        // is not contest's problem
        if (contestId == null) {
            return;
        } else {
            // if contest privilege is PUBLIC
            Optional<ContestEntity> entity = Optional.of(contestService.getById(contestId));
            ContestPrivilege privilege = entity.orElseThrow(() ->
                new NotFoundException("not found this contest, id is" + contestId))
                .getPrivilege();
            if (privilege.equals(ContestPrivilege.PUBLIC)) {
                return;
            }
            // not public contest
            if (userId == null) {
                checkPrivilegePassed = false;
            } else {
                Optional<ContestPrivilegeEntity> res = contestPrivilegeRepository
                    .findByContestIdAndUserIdAndDeleteAt(contestId, userId, Const.TIME_ZERO);
                if (res.isPresent()) {
                    if (res.get().getType().getValue() < ContestPrivilegeTypeEnum.ALLOW_TAKE_PART_IN.getValue()) {
                        checkPrivilegePassed = false;
                    }
                } else {
                    checkPrivilegePassed = false;
                }
            }
        }
        if (!checkPrivilegePassed) {
            throw new UnauthorizedException("You have no right to take part in the contest");
        }
    }

    private ProblemDescVO getCount(ProblemDescVO vo, Long id, Long contestId) {
        return contestId == null ? getProblemCount(vo, id) :
            getContestProblemCount(vo, id, contestId);
    }

    private ProblemDescVO getContestProblemCount(ProblemDescVO vo, Long id, Long contestId) {
        vo.setAcCount(countService.fetchCount(
            CacheUtil.defaultKey(
                CacheUtil.pair(ContestEntity.class, contestId),
                CacheUtil.pair(ProblemEntity.class, id),
                KeyPrefix.PROBLEM_CONTEST_ACCEPTED
            )
        ));
        vo.setSubmitCount(countService.fetchCount(
            CacheUtil.defaultKey(
                CacheUtil.pair(ContestEntity.class, contestId),
                CacheUtil.pair(ProblemEntity.class, id),
                KeyPrefix.PROBLEM_CONTEST_SUBMIT_COUNT
            )
        ));
        return vo;
    }
    private ProblemDescVO getProblemCount(ProblemDescVO vo, Long id) {
        vo.setAcCount(countService.fetchCount(
            CacheUtil.defaultKey(
                ProblemEntity.class, id, KeyPrefix.PROBLEM_TOTAL_ACCEPT
            )
        ));
        vo.setSubmitCount(countService.fetchCount(
            CacheUtil.defaultKey(
                ProblemEntity.class, id, KeyPrefix.PROBLEM_TOTAL_SUBMIT
            )
        ));
        return vo;
    }

    private final ProblemDescRepository descRepository;
    private final SolutionRepository solutionRepository;
    @Resource
    ContestPrivilegeRepository contestPrivilegeRepository;

    @Resource
    ContestService contestService;

    protected ProblemServiceImpl(ProblemDescRepository descRepository,
                                 ProblemRepository repository,
                                 SolutionRepository solutionRepository) {
        super(repository);
        this.descRepository = descRepository;
        this.solutionRepository = solutionRepository;
    }
}
