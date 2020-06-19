package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.cache.CacheStore;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.constant.KeyPrefixConstant;
import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
import cn.edu.sdtbu.model.dto.LongPair;
import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.model.properties.OnlineJudgeProperties;
import cn.edu.sdtbu.service.*;
import cn.edu.sdtbu.util.CacheUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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
    CountService    countService;
    @Resource
    UserService userService;
    @Resource
    ProblemService problemService;
    @Resource
    SolutionService solutionService;
    @Resource
    CacheHandler    handler;
    @Resource
    OnlineJudgeProperties properties;
    @Override
    public void refreshRankList(Boolean reloadCount) {
        Map<Long, UserEntity> userEntities = userService.listAll().stream()
            // 过滤被删除用户
            .filter(o -> o.getDeleteAt().equals(OnlineJudgeConstant.TIME_ZERO))
            .collect(Collectors.toMap(UserEntity::getId, o -> o));
        Map<Long, LongPair> userMap = userEntities.values()
            .stream()
            .collect(Collectors.toMap(
                UserEntity::getId, o -> LongPair.of(o.getAcceptedCount(), o.getSubmitCount())));

        // 和ProblemEntity分开处理, 减少GC压力防止OOM
        if (reloadCount) {
            userMap = reloadUserSubmitCount(userMap);
        }

        Map<String, Double> rankMap          = Maps.newHashMap();

        userMap.forEach((f, r) -> {
            rankMap.put(f.toString(), CacheUtil.rankListScore(r.getFirst(), r.getSecond()));
            UserEntity entity = userEntities.get(f);
            entity.setAcceptedCount(r.getFirst());
            entity.setSubmitCount(r.getSecond());
        });
        cache().delete(KeyPrefixConstant.USERS_RANK_LIST_DTO);
        cache().sortedListAdd(KeyPrefixConstant.USERS_RANK_LIST_DTO, rankMap);
        userService.saveAll(userEntities.values());
    }

    private Map<Long, LongPair> reloadUserSubmitCount(Map<Long, LongPair> userMap) {
        long total = 0L;
        // problemId - <accepted, submitted> map
        Map<Long, LongPair>  pairMap  = Maps.newHashMap();
        // problemId - is init
        Map<Long, Boolean> initMap = Maps.newHashMap();
        // userId - <accepted, submitted> map
        List<SolutionEntity> solutionEntities;
        Map<Long, ProblemEntity> problemEntityMap = problemService.listAll().stream().collect(
            Collectors.toMap(ProblemEntity::getId, o -> o));


        int                  page     = 0;
        log.info("开始重新统计各类提交数, 每次加载{}条数据", properties.getReloadPageSize());
        // 按页进行处理， 防止OOM
        do {
            Pageable             pageable = PageRequest.of(page, properties.getReloadPageSize(), Sort.unsorted());

            solutionEntities = solutionService.listAll(pageable).getContent();
            if (solutionEntities.isEmpty()) {
                break;
            }
            //统计该页所有计数情况
            solutionEntities.forEach(i -> {
                LongPair pair = pairMap.getOrDefault(i.getProblemId(), LongPair.of(0L, 0L));
                LongPair userPair = userMap.getOrDefault(i.getOwnerId(), LongPair.of(0L, 0L));

                if (i.getResult().equals(JudgeResult.ACCEPT)) {
                    pair.addFirst();
                    userPair.addFirst();
                }
                userPair.addSecond();
                pair.addSecond();
                userMap.put(i.getOwnerId(), userPair);
                pairMap.put(i.getProblemId(), pair);
            });

            // 将统计数据更新到 ProblemEntity
            pairMap.forEach((f, s) -> {
                ProblemEntity entity = problemEntityMap.get(f);
                if (initMap.containsKey(f)) {
                    entity.setAcceptedCount(s.getFirst() + entity.getAcceptedCount());
                    entity.setSubmitCount(s.getSecond() + entity.getSubmitCount());
                } else {
                    entity.setAcceptedCount(s.getFirst());
                    entity.setSubmitCount(s.getSecond());
                    initMap.put(f, true);
                }
            });
            total += solutionEntities.size();
            //下一页
            log.info("第{}页已经被成功统计, 目前为止已经统计{}次提交", page, total);
            page++;
        } while (true);
        problemService.saveAll(problemEntityMap.values());
        return userMap;
    }

    private CacheStore<String, String> cache() {
        return handler.fetchCacheStore();
    }

}
