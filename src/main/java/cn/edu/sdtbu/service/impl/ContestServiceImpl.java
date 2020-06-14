package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.exception.BadRequestException;
import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.constant.ExceptionConstant;
import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
import cn.edu.sdtbu.model.dto.ContestPrivilegeInfoDTO;
import cn.edu.sdtbu.model.entity.contest.*;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.ContestPrivilege;
import cn.edu.sdtbu.model.enums.ContestPrivilegeTypeEnum;
import cn.edu.sdtbu.model.enums.ContestStatus;
import cn.edu.sdtbu.model.enums.LangType;
import cn.edu.sdtbu.model.param.ContestParam;
import cn.edu.sdtbu.model.param.ContestProblemParam;
import cn.edu.sdtbu.model.vo.ProblemDescVO;
import cn.edu.sdtbu.model.vo.contest.*;
import cn.edu.sdtbu.repository.contest.*;
import cn.edu.sdtbu.repository.user.UserClassRepository;
import cn.edu.sdtbu.service.ContestProblemService;
import cn.edu.sdtbu.service.ContestService;
import cn.edu.sdtbu.service.ProblemService;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.SpringUtil;
import cn.edu.sdtbu.util.TimeUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-18 07:36
 */
@Service
public class ContestServiceImpl extends AbstractBaseService<ContestEntity, Long> implements ContestService {

    @Resource
    ContestProblemRepository   contestProblemRepository;
    @Resource
    ContestIpLimitRepository   ipLimitRepository;
    @Resource
    ContestProblemService      contestProblemService;
    @Resource
    ContestResultRepository    contestResultRepository;
    @Resource
    ProblemService             problemService;
    @Resource
    UserService                userService;
    @Resource
    UserClassRepository        userClassRepository;
    @Resource
    ContestAllowLangRepository allowLangRepository;
    @Resource
    ContestPrivilegeRepository privilegeRepository;

    protected ContestServiceImpl(ContestRepository repository) {
        super(repository);
    }

    @Override
    public ProblemDescVO getContestProblemDesc(long contest, int order, Long userId) {
        ContestProblemEntity entity = contestProblemService.getContestProblem(contest, order);
        if (entity == null) {
            throw ExceptionConstant.NOT_FOUND;
        }
        return problemService.getProblemDescVoById(new ProblemDescVO(), entity.getProblemId(), contest, userId);
    }

    @Override
    public Page<ContestsVO> fetchContests(UserEntity userEntity, Pageable page) {
        Page<ContestEntity> contestPage  = listAll(page);
        List<ContestEntity> list         = contestPage.getContent();
        List<ContestsVO>    content      = new LinkedList<>();
        boolean             notLogin     = userEntity == null;
        Map<Long, Boolean>  contestIdMap = new HashMap<>();
        if (!notLogin) {
            contestIdMap = privilegeRepository.findAllByContestIdInAndUserIdAndDeleteAt(
                list.stream().map(ContestEntity::getId).collect(Collectors.toSet()),
                userEntity.getId(),
                OnlineJudgeConstant.TIME_ZERO
            ).stream().collect(Collectors.toMap(ContestPrivilegeEntity::getContestId, i -> true));
        }
        final Map<Long, Boolean> finalContestIdMap = contestIdMap;
        list.forEach(item -> {
            ContestsVO vo = new ContestsVO();
            SpringUtil.cloneWithoutNullVal(item, vo);

            vo.setAllowed(notLogin ? item.getPrivilege().equals(ContestPrivilege.PUBLIC) :
                finalContestIdMap.getOrDefault(item.getId(), item.getOwnerId().equals(userEntity.getId())));

            vo.setOwner(userService.getById(item.getOwnerId()).getUsername());
            vo.setStatus(getContestStatus(item.getStartAt(), item.getEndBefore()));
            content.add(vo);
        });
        return new PageImpl<>(content, page, contestPage.getTotalElements());
    }

    @Override
    public ContestDetailVO fetchDetailContestInfo(long contestId, Long userId) {
        ContestDetailVO vo     = new ContestDetailVO();
        ContestEntity   entity = getById(contestId);
        SpringUtil.cloneWithoutNullVal(entity, vo);
        UserEntity owner = userService.getById(entity.getOwnerId());
        vo.setOwner(owner.getNickname());
        vo.setStatus(getContestStatus(entity.getStartAt(), entity.getEndBefore()));

        // set problems and submit/accepted count
        List<ContestProblemVO>     problems        = new LinkedList<>();
        List<ContestProblemEntity> problemEntities = contestProblemService.listAllContestProblems(contestId);
        List<ContestResultEntity>  contestResults  = contestResultRepository.findAllByContestId(contestId);
        Map<Integer, Long>         submitCount     = new HashMap<>();
        Map<Integer, Long>         acceptedCount   = new HashMap<>();
        Map<Integer, Boolean>      userIsAccepted  = new HashMap<>();
        contestResults.forEach(i -> {
            submitCount.put(i.getProblemOrder(), i.getSubmitCount() + submitCount.getOrDefault(i.getProblemOrder(), 0L));
            if (!i.getAcAt().equals(OnlineJudgeConstant.TIME_ZERO)) {
                acceptedCount.put(i.getProblemOrder(), 1L + acceptedCount.getOrDefault(i.getProblemOrder(), 0L));
                if (i.getUserId().equals(userId)) {
                    userIsAccepted.put(i.getProblemOrder(), true);
                }
            }
        });

        problemEntities.forEach(i -> {
            ContestProblemVO contestProblemVO = new ContestProblemVO();
            contestProblemVO.setTitle(i.getTitle());
            contestProblemVO.setOrder(i.getProblemOrder());
            contestProblemVO.setAcCount(acceptedCount.getOrDefault(i.getProblemOrder(), 0L));
            contestProblemVO.setSubmitCount(submitCount.getOrDefault(i.getProblemOrder(), 0L));
            contestProblemVO.setIsAccepted(userIsAccepted.getOrDefault(i.getProblemOrder(), false));
            problems.add(contestProblemVO);
        });
        vo.setProblems(problems);
        return vo;
    }

    @Override
    public void createContest(ContestParam param, UserEntity user) {
        ContestEntity contestEntity = saveContestEntity(param, user);
        saveLangTypes(param.getAllowLang(), contestEntity.getId());
        saveContestPrivilege(param.getClassIds(),
            param.getAllowUsernames(),
            param.getDenyUsernames(),
            contestEntity.getId());

        if (!collectionsIsEmpty(param.getAllowIps(), param.getDenyIps())) {
            ContestIpLimitEntity ipLimitEntity = new ContestIpLimitEntity();
            ipLimitEntity.setAllowIps(JSON.toJSONString(param.getAllowIps()));
            ipLimitEntity.setDenyIps(JSON.toJSONString(param.getDenyIps()));
            ipLimitEntity.setContestId(contestEntity.getId());
            ipLimitRepository.save(ipLimitEntity);
        }
        saveContestProblem(param.getProblems(), contestEntity.getId());

    }

    @Override
    public Page<StandingNodeVO> getStandings(long contestId) {
        List<ContestResultEntity> results   = contestResultRepository.findAllByContestId(contestId);
        Timestamp                 startTime = getById(contestId).getStartAt();
        Map<Long, StandingNodeVO> nodeVOMap = fetchProblemResMap(results, startTime.getTime());
        Map<Long, UserEntity> users = userService.getByIds(nodeVOMap.keySet(), Pageable.unpaged()).getContent()
            .stream().collect(Collectors.toMap(UserEntity::getId, i -> i));
        List<StandingNodeVO> res = new LinkedList<>(nodeVOMap.values());
        res.sort((o1, o2) -> {
            if (o1.getSolved().equals(o2.getSolved())) {
                return (int) (o1.getPenalty() - o2.getPenalty());
            }
            return o2.getSolved() - o1.getSolved();
        });
        res.forEach(i -> {
            UserEntity user = users.get(i.getUserId());
            if (user == null) {
                throw new NotFoundException("not found user, id is " + i.getUserId());
            }
            i.setUsername(user.getUsername());
            i.setNickname(user.getNickname());
        });
        return new PageImpl<>(res);
    }

    /**
     * @param resultEntities all contest results(indexed by contestId)
     * @return <userId, StandingNode> map
     */
    private Map<Long, StandingNodeVO> fetchProblemResMap(List<ContestResultEntity> resultEntities, long contestStartTime) {
        // <userId, problemSubmitResult>
        Map<Long, LinkedList<StandingProblemNodeVO>> problemResMap = new HashMap<>();
        // <userId, standingNode>
        Map<Long, StandingNodeVO> res = new HashMap<>();
        resultEntities.forEach(i -> {
            StandingNodeVO nodeVO = res.get(i.getUserId());
            if (nodeVO == null) {
                nodeVO = new StandingNodeVO();
            }
            // Accepted this problem
            nodeVO.setUserId(i.getUserId());
            if (!i.getAcAt().equals(OnlineJudgeConstant.TIME_ZERO)) {
                nodeVO.setSolved((nodeVO.getSolved() == null ? 0 : nodeVO.getSolved()) + 1);
                nodeVO.setPenalty((nodeVO.getPenalty() == null ? 0L : nodeVO.getPenalty()) +
                    i.getAcAt().getTime() - contestStartTime + i.getSubmitCount() * TimeUnit.MINUTES.toMillis(20));
            }
            LinkedList<StandingProblemNodeVO> list = problemResMap.get(i.getUserId());
            if (list == null) {
                list = new LinkedList<>();
            }
            StandingProblemNodeVO standingProblemNodeVO = new StandingProblemNodeVO();
            if (!i.getAcAt().equals(OnlineJudgeConstant.TIME_ZERO)) {
                standingProblemNodeVO.setAcAt(i.getAcAt().getTime() - contestStartTime);
            }
            standingProblemNodeVO.setOrder(i.getProblemOrder());
            standingProblemNodeVO.setSubmitCount(i.getSubmitCount());
            list.add(standingProblemNodeVO);
            problemResMap.put(i.getUserId(), list);
            res.put(i.getUserId(), nodeVO);
        });
        problemResMap.forEach((f, s) -> {
            StandingNodeVO vo = res.get(f);
            s.sort(Comparator.comparingInt(StandingProblemNodeVO::getOrder));
            vo.setProblemResults(s);
            res.put(f, vo);
        });
        return res;
    }

    private void saveContestProblem(List<ContestProblemParam> vos, Long contestId) {
        if (CollectionUtils.isEmpty(vos)) {
            throw new BadRequestException("please chose at least a problem");
        }
        List<ContestProblemEntity> list  = new LinkedList<>();
        AtomicInteger              order = new AtomicInteger(1);

        vos.forEach(item -> {
            // problem must exist
            problemService.mustExistById(item.getId());

            ContestProblemEntity entity = new ContestProblemEntity();
            entity.setContestId(contestId);
            if (item.getId() == null) {
                throw new BadRequestException("problem id must not be empty");
            }
            entity.setProblemId(item.getId());
            entity.setProblemOrder(order.getAndIncrement());
            entity.setTitle(item.getTitle() == null ?
                problemService.getById(item.getId()).getTitle() : item.getTitle());
            list.add(entity);
        });
        contestProblemRepository.saveAll(list);
    }

    private void saveContestPrivilege(List<Long> classIds,
                                      List<String> allowUsernames,
                                      List<String> denyUsernames,
                                      Long contestId) {
        Set<Long> userIds = new TreeSet<>();
        if (CollectionUtils.isNotEmpty(classIds)) {
            userClassRepository.findAllByClassIdInAndDeleteAt(classIds, OnlineJudgeConstant.TIME_ZERO)
                .forEach(item -> userIds.add(item.getUserId()));
        }
        if (CollectionUtils.isNotEmpty(allowUsernames)) {
            allowUsernames.forEach(item -> userIds.add(userService.getByUsername(item).getId()));
        }
        List<ContestPrivilegeEntity> list = new LinkedList<>();

        userIds.forEach(allowedId -> {
            ContestPrivilegeEntity entity = new ContestPrivilegeEntity();
            entity.setType(ContestPrivilegeTypeEnum.ALLOW_TAKE_PART_IN);
            entity.setContestId(contestId);
            entity.setUserId(allowedId);
            list.add(entity);
        });
        if (denyUsernames != null) {
            denyUsernames.forEach(deny -> {
                ContestPrivilegeEntity entity = new ContestPrivilegeEntity();
                entity.setType(ContestPrivilegeTypeEnum.DENY_TAKE_PART_IN);
                entity.setContestId(contestId);
                entity.setUserId(userService.getByUsername(deny).getId());
                list.add(entity);
            });
        }
        if (CollectionUtils.isNotEmpty(list)) {
            privilegeRepository.saveAll(list);
        }
    }

    private void saveLangTypes(List<LangType> types, Long contestId) {
        // clear old settings
        List<AllowLangEntity> allowLangEntities = allowLangRepository.findAllByContestIdAndDeleteAt(contestId, OnlineJudgeConstant.TIME_ZERO);
        allowLangEntities.forEach(item -> item.setDeleteAt(TimeUtil.now()));
        allowLangRepository.saveAll(allowLangEntities);


        List<AllowLangEntity> list = new LinkedList<>();
        if (CollectionUtils.isEmpty(types)) {
            types = new LinkedList<>();
            types.addAll(Arrays.asList(LangType.values()));
        }
        for (LangType type : types) {
            AllowLangEntity entity = new AllowLangEntity();
            entity.setDeleteAt(OnlineJudgeConstant.TIME_ZERO);
            entity.setContestId(contestId);
            entity.setLang(type);
            list.add(entity);
        }
        allowLangRepository.saveAll(list);
    }

    private ContestEntity saveContestEntity(ContestParam param, UserEntity user) {
        ContestEntity entity = new ContestEntity();

        entity.setEndBefore(new Timestamp(param.getEndBefore()));
        entity.setLockRankAt(param.getLockRankAt() == null ? entity.getEndBefore() : new Timestamp(param.getLockRankAt()));
        entity.setName(param.getName());
        entity.setStartAt(new Timestamp(param.getStartAt()));
        entity.setOwnerId(user.getId());
        entity.setPrivilege(param.getPrivilege());
        entity.setPrivilegeInfo(JSON.toJSONString(new ContestPrivilegeInfoDTO(
            param.getPassword(),
            param.getRegisterBegin(),
            param.getRegisterEnd()
        )));
        entity.setRule(param.getContestRule());
        return save(entity);
    }

    private ContestStatus getContestStatus(Timestamp start, Timestamp end) {
        Timestamp now = TimeUtil.now();
        if (now.before(start)) {
            return ContestStatus.PENDING;
        } else if (now.after(end)) {
            return ContestStatus.ENDED;
        }
        return ContestStatus.RUNNING;
    }

    private boolean collectionsIsEmpty(Collection... collections) {
        for (Collection collection : collections) {
            if (CollectionUtils.isNotEmpty(collection)) {
                return true;
            }
        }
        return false;
    }
}
