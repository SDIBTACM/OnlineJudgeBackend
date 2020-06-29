package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.cache.CacheStore;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.constant.KeyPrefixConstant;
import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
import cn.edu.sdtbu.model.dto.CountPair;
import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.model.enums.RankType;
import cn.edu.sdtbu.model.properties.OnlineJudgeProperties;
import cn.edu.sdtbu.service.ProblemService;
import cn.edu.sdtbu.service.RefreshService;
import cn.edu.sdtbu.service.SolutionService;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.CacheUtil;
import cn.edu.sdtbu.util.TimeUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
    UserService           userService;
    @Resource
    ProblemService        problemService;
    @Resource
    SolutionService       solutionService;
    @Resource
    CacheHandler          handler;
    @Resource
    OnlineJudgeProperties properties;

    @Override
    public void refreshOverAllRankList(Boolean reloadCount, boolean freshUserEntity) {
        Map<Long, UserEntity> userEntities = fetchUserMap();
        Map<Long, CountPair>  userMap;

        // 和ProblemEntity分开处理, 减少GC压力防止OOM
        if (reloadCount) {
            userMap = reCountUserSubmitWithProblemCount();
        } else {
            userMap = userEntities.values()
                .stream()
                .collect(Collectors.toMap(
                    UserEntity::getId, o -> CountPair.of(o.getAcceptedCount(), o.getSubmitCount())));
        }
        Map<String, Double> rankMap = userMap2RankMap(userMap, userEntities);
        cache().sortedListAdd(KeyPrefixConstant.RANK_TYPE.get(RankType.OVERALL), rankMap);
        if (freshUserEntity) {
            userService.saveAll(userEntities.values());
        }
    }

    @Override
    public void reloadRankList(RankType type, boolean isAdd) {
        Map<Long, CountPair> userCount = countUserSubmit(type);
        Map<String, Double> rank = userMap2RankMap(userCount);
        if (!isAdd) {
            for (Map.Entry<String, Double> e : rank.entrySet()) {
                CountPair pair = CacheUtil.parseCountPair(e.getValue());
                e.setValue(CacheUtil.RATIO * pair.getSubmit() - pair.getAccept());
            }
        }
        cache().sortedListAdd(KeyPrefixConstant.RANK_TYPE.get(type), rank);
    }

    private Map<Long, UserEntity> fetchUserMap() {
        return userService.listAll().stream()
            // 过滤被删除用户
            .filter(o -> o.getDeleteAt().equals(OnlineJudgeConstant.TIME_ZERO))
            .collect(Collectors.toMap(UserEntity::getId, o -> o));
    }


    private Map<String, Double> userMap2RankMap(Map<Long, CountPair> userMap) {
        return userMap2RankMap(userMap, fetchUserMap());
    }

    private Map<String, Double> userMap2RankMap(Map<Long, CountPair> userMap, Map<Long, UserEntity> userEntities) {
        Map<String, Double> rankMap = Maps.newHashMap();
        final CountPair     zero    = CountPair.of(0L, 0L);
        for (Map.Entry<Long, UserEntity> id : userEntities.entrySet()) {
            CountPair pir = userMap.getOrDefault(id.getKey(), zero);
            rankMap.put(id.getKey().toString(), CacheUtil.parseScore(pir.getAccept(), pir.getSubmit()));
            id.getValue().setAcceptedCount(pir.getAccept());
            id.getValue().setSubmitCount(pir.getSubmit());
        }
        return rankMap;
    }
    /**
     * 获取用户start-end期间内提交的记录
     */
    private Map<Long, CountPair> countUserSubmit(RankType rankType) {
        Pair<Timestamp, Timestamp> time    = fetchTimeBeforeNow(rankType);
        Map<Long, CountPair>       userMap = Maps.newHashMap();
        List<SolutionEntity>       solutionEntities;
        int                        page    = 0;
        do {
            Pageable pageable = PageRequest.of(page, properties.getReloadPageSize(), Sort.unsorted());
            solutionEntities = solutionService.listByTimeBetween(time.getLeft(), time.getRight(), pageable).getContent();
            if (solutionEntities.isEmpty()) {
                break;
            }
            //统计该页所有计数情况
            solutionEntities.forEach(i -> {
                CountPair userPair = userMap.getOrDefault(i.getOwnerId(), CountPair.of(0L, 0L));

                if (i.getResult().equals(JudgeResult.ACCEPT)) {
                    userPair.accepted();
                }
                userPair.submitted();
                userMap.put(i.getOwnerId(), userPair);
            });
            page++;
        } while (true);
        return userMap;
    }

    /**
     * 获取用户所有提交记录,并将problem的计数信息刷新
     */
    private Map<Long, CountPair> reCountUserSubmitWithProblemCount() {
        Map<Long, CountPair> userMap = Maps.newHashMap();
        // 时间节点， 查询此时间点之间的提交记录
        Pair<Timestamp, Timestamp> time = fetchTimeBeforeNow(RankType.OVERALL);

        long total = 0L;
        // problemId - <accepted, submitted> map
        Map<Long, CountPair> pairMap = Maps.newHashMap();
        // problemId - is init
        Map<Long, Boolean> initMap = Maps.newHashMap();
        // userId - <accepted, submitted> map
        List<SolutionEntity> solutionEntities;
        Map<Long, ProblemEntity> problemEntityMap = problemService.listAll().stream().collect(
            Collectors.toMap(ProblemEntity::getId, o -> o));

        int page = 0;
        log.info("开始重新统计各类提交数, 每次加载{}条数据", properties.getReloadPageSize());
        // 按页进行处理， 防止OOM
        do {
            Pageable pageable = PageRequest.of(page, properties.getReloadPageSize(), Sort.unsorted());
            solutionEntities = solutionService.listByTimeBetween(time.getLeft(), time.getRight(), pageable).getContent();
            if (solutionEntities.isEmpty()) {
                break;
            }
            //统计该页所有计数情况
            solutionEntities.forEach(i -> {
                CountPair pair     = pairMap.getOrDefault(i.getProblemId(), CountPair.of(0L, 0L));
                CountPair userPair = userMap.getOrDefault(i.getOwnerId(), CountPair.of(0L, 0L));

                if (i.getResult().equals(JudgeResult.ACCEPT)) {
                    pair.accepted();
                    userPair.accepted();
                }
                userPair.submitted();
                pair.submitted();
                userMap.put(i.getOwnerId(), userPair);
                pairMap.put(i.getProblemId(), pair);
            });

            // 将统计数据更新到 ProblemEntity
            pairMap.forEach((f, s) -> {
                ProblemEntity entity = problemEntityMap.get(f);
                if (initMap.containsKey(f)) {
                    entity.setAcceptedCount(s.getAccept() + entity.getAcceptedCount());
                    entity.setSubmitCount(s.getSubmit() + entity.getSubmitCount());
                } else {
                    entity.setAcceptedCount(s.getAccept());
                    entity.setSubmitCount(s.getSubmit());
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

    private Pair<Timestamp, Timestamp> fetchTimeBeforeNow(RankType type) {
        Timestamp now       = TimeUtil.now();
        long      timestamp = now.getTime();
        switch (type) {
            case DAILY:
                timestamp -= TimeUnit.DAYS.toMillis(1);
                break;
            case WEEKLY:
                timestamp -= TimeUnit.DAYS.toMillis(7);
                break;
            case MONTHLY:
                timestamp -= TimeUnit.DAYS.toMillis(30);
                break;
            case OVERALL:
                timestamp = OnlineJudgeConstant.TIME_ZERO.getTime();
                break;
            default:
        }
        return Pair.of(new Timestamp(timestamp), now);
    }

    private CacheStore<String, String> cache() {
        return handler.fetchCacheStore();
    }

}
