package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.contest.ContestProblemEntity;
import cn.edu.sdtbu.model.enums.KeyPrefix;
import cn.edu.sdtbu.repository.contest.ContestProblemRepository;
import cn.edu.sdtbu.service.ContestProblemService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.CacheUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-05 15:16
 */
@Service
public class ContestProblemServiceImpl extends AbstractBaseService<ContestProblemEntity, Long> implements ContestProblemService {

    @Override
    public ContestProblemEntity getContestProblem(long contestId, int order) {
        String key = CacheUtil.defaultKey(
            Pair.of(Long.class, contestId + ""),
            Pair.of(Integer.class, order + ""),
            KeyPrefix.CONTEST_PROBLEM
        );
        String res;
        if ((res = cache().get(key)) != null) {
            return JSON.parseObject(res, ContestProblemEntity.class);
        } else {
            ContestProblemEntity entity = repository.findByContestIdAndProblemOrder(contestId, order);
            cache().put(key, JSON.toJSONString(entity));
            return entity;
        }
    }

    @Override
    public List<ContestProblemEntity> listAllContestProblems(long contestId) {
        return repository.findAllByContestId(contestId);
    }

    @Resource
    ContestProblemRepository repository;

    protected ContestProblemServiceImpl(ContestProblemRepository repository) {
        super(repository);
    }
}
