package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.cache.CacheStore;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.constant.KeyPrefixConstant;
import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
import cn.edu.sdtbu.model.entity.CountEntity;
import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.repository.CountRepository;
import cn.edu.sdtbu.repository.user.UserRepository;
import cn.edu.sdtbu.service.CountService;
import cn.edu.sdtbu.service.RefreshService;
import cn.edu.sdtbu.service.SolutionService;
import cn.edu.sdtbu.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-25 20:51
 */
@Slf4j
@Service
public class RefreshServiceImpl implements RefreshService {

    @Resource
    UserRepository  userRepository;
    @Resource
    CountRepository countRepository;
    @Resource
    CountService    countService;
    @Resource
    SolutionService solutionService;
    @Resource
    CacheHandler    handler;

    @Override
    public void refreshRankList(Boolean reloadCount) {
        if (reloadCount) {
            reloadUserSubmitCount();
        }

        Map<String, Long>   totalSubmitMap   = countService.fetchByKeyLike(CacheUtil.defaultKey(UserEntity.class, "%", KeyPrefixConstant.USER_SUBMIT_COUNT));
        Map<String, Long>   totalAcceptCount = countService.fetchByKeyLike(CacheUtil.defaultKey(UserEntity.class, "%", KeyPrefixConstant.USER_ACCEPTED_COUNT));
        Map<String, Double> rankMap          = new HashMap<>(totalSubmitMap.size());

        int       submitPrefixLength = CacheUtil.defaultKey(UserEntity.class, "", KeyPrefixConstant.USER_SUBMIT_COUNT).length();
        Set<Long> usedId             = new HashSet<>();
        totalSubmitMap.forEach((f, s) -> {
            String id       = f.substring(submitPrefixLength);
            Long   accepted = totalAcceptCount.getOrDefault(CacheUtil.defaultKey(UserEntity.class, usedId, KeyPrefixConstant.USER_ACCEPTED_COUNT), 0L);
            rankMap.put(id, CacheUtil.rankListScore(accepted, s));
            usedId.add(Long.parseLong(id));
        });

        // users who not submit solution
        Double zero = 0.000000;
        userRepository.findAllByDeleteAt(OnlineJudgeConstant.TIME_ZERO).forEach(i -> {
            if (!usedId.contains(i.getId())) {
                rankMap.put(i.getId() + "", zero);
            }
        });
        cache().delete(KeyPrefixConstant.USERS_RANK_LIST_DTO);
        cache().sortedListAdd(KeyPrefixConstant.USERS_RANK_LIST_DTO, rankMap);
    }

    private void reloadUserSubmitCount() {
        Map<String, Long> map = new HashMap<>();
        solutionService.listAll().forEach(i -> {
            String key;
            if (i.getResult().equals(JudgeResult.ACCEPT)) {
                key = CacheUtil.defaultKey(UserEntity.class, i.getOwnerId(), KeyPrefixConstant.USER_ACCEPTED_COUNT);
                map.put(key, map.getOrDefault(key, 0L) + 1);
            }
            key = CacheUtil.defaultKey(UserEntity.class, i.getOwnerId(), KeyPrefixConstant.USER_SUBMIT_COUNT);
            map.put(key, map.getOrDefault(key, 0L) + 1);
        });
        Map<String, CountEntity> countEntities = countRepository.findAllByCountKeyIn(map.keySet())
            .stream().collect(Collectors.toMap(CountEntity::getCountKey, i -> i));
        map.forEach((f, s) -> {
            CountEntity countEntity;
            if (countEntities.containsKey(f)) {
                countEntity = countEntities.get(f);
            } else {
                countEntity = new CountEntity();
                countEntity.setCountKey(f);
            }
            countEntity.setTotal(s);
            countEntities.put(countEntity.getCountKey(), countEntity);
        });
        countRepository.saveAll(countEntities.values());
    }

    @Override
    public void refreshSolutionCount(Long problemId) {
        List<SolutionEntity> solutions;
        if (problemId == null) {
            solutions = solutionService.listAll();
        } else {
            solutions = solutionService.findAllByProblemId(problemId);
        }
        Map<Long, Long> problemAccepted  = new HashMap<>();
        Map<Long, Long> problemSubmitted = new HashMap<>();
        solutions.forEach(i -> {
            if (i.getResult().equals(JudgeResult.ACCEPT)) {
                problemAccepted.put(i.getProblemId(), problemAccepted.getOrDefault(i.getProblemId(), 0L) + 1);
            }
            problemSubmitted.put(i.getProblemId(), problemSubmitted.getOrDefault(i.getProblemId(), 0L) + 1);
        });
        problemSubmitted.forEach((f, s) -> {
            countService.setCount(CacheUtil.defaultKey(
                ProblemEntity.class, f, KeyPrefixConstant.PROBLEM_TOTAL_ACCEPT), problemAccepted.getOrDefault(f, 0L));
            countService.setCount(CacheUtil.defaultKey(
                ProblemEntity.class, f, KeyPrefixConstant.PROBLEM_TOTAL_SUBMIT), s);

        });
        log.info("problem solution count refresh completed, problem id is {}",
            problemId == null ? "all" : problemId);
    }

    private CacheStore<String, String> cache() {
        return handler.fetchCacheStore();
    }

}
