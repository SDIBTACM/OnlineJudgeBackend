package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.constant.KeyPrefixConstant;
import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.model.enums.RankType;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.vo.SolutionListNode;
import cn.edu.sdtbu.model.vo.base.BaseUserVO;
import cn.edu.sdtbu.repository.SolutionRepository;
import cn.edu.sdtbu.repository.user.UserRepository;
import cn.edu.sdtbu.service.SolutionService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.CacheUtil;
import cn.edu.sdtbu.util.SpringUtil;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 20:29
 */
@Service
public class SolutionServiceImpl extends AbstractBaseService<SolutionEntity, Long> implements SolutionService {
    @Resource
    SolutionRepository solutionRepository;
    @Resource
    UserRepository     userRepository;
    protected SolutionServiceImpl(SolutionRepository repository) {
        super(repository);
    }

    @Override
    public Page<SolutionEntity> listByTimeBetween(Timestamp start, Timestamp end, Pageable pageable) {
        return solutionRepository.findAllByCreateAtBetween(start, end, pageable);
    }

    @Override
    public Page<SolutionListNode> listSubmit(SolutionEntity query, UserRole role, Pageable pageable) {
        Page<SolutionEntity>   page = solutionRepository.findAll(Example.of(query), pageable);
        List<SolutionListNode> list = new LinkedList<>();

        Map<Long, UserEntity> idEntityMap = userRepository.findAllByIdInAndDeleteAt(
            page.getContent().stream().map(SolutionEntity::getOwnerId).collect(Collectors.toSet()),
            OnlineJudgeConstant.TIME_ZERO)
            .stream().collect(Collectors.toMap(UserEntity::getId, u -> u));

        page.getContent().forEach(i -> {
            SolutionListNode node = new SolutionListNode();
            SpringUtil.cloneWithoutNullVal(i, node);
            if (role.getValue() < UserRole.TEACHER.getValue()) {
                node.setSimilarAt(null);
                node.setSimilarPercent(null);
            }
            node.setUserInfo(BaseUserVO.fetchBaseFromEntity(idEntityMap.get(i.getOwnerId())));
            list.add(node);
        });
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    @Override
    public List<SolutionEntity> findAllByProblemId(Long problemId) {
        return solutionRepository.findAllByProblemId(problemId);
    }

    @Override
    public Boolean existsByOwnerIdAndProblemIdAndResult(Long userId, Long id, JudgeResult accept) {
        return solutionRepository.existsByOwnerIdAndProblemIdAndResult(userId, id, accept);
    }

    @Override
    public Boolean existsByOwnerIdAndProblemIdAndContestIdAndResult(Long userId, Long id, Long contestId, JudgeResult accept) {
        return solutionRepository.existsByOwnerIdAndProblemIdAndContestIdAndResult(userId, id, contestId, accept);
    }

    @Override
    public List<SolutionEntity> findAllByOwnerId(Long userId) {
        return solutionRepository.findAllByOwnerId(userId);
    }

    @Override
    public List<SolutionEntity> findAllByOwnerIdAndResultAndProblemIdIn(Long id, JudgeResult accept, List<Long> problemIds) {
        return solutionRepository.findAllByOwnerIdAndResultAndProblemIdIn(id, accept, problemIds);
    }

    @Override
    public void incSubmitCode(long userId) {
        for (RankType type : RankType.values()) {
            cache().sortedListAdd(KeyPrefixConstant.RANK_TYPE.get(type), userId + "", CacheUtil.RATIO * -1);
        }
    }
}
