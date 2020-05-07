package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.model.enums.SolutionResult;
import cn.edu.sdtbu.model.param.ProblemParam;
import cn.edu.sdtbu.model.vo.ProblemSimpleListVO;
import cn.edu.sdtbu.model.vo.UserCenterVO;
import cn.edu.sdtbu.repository.ProblemDescRepository;
import cn.edu.sdtbu.repository.ProblemRepository;
import cn.edu.sdtbu.repository.SolutionRepository;
import cn.edu.sdtbu.service.ProblemService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import cn.edu.sdtbu.util.SpringUtil;
import cn.edu.sdtbu.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-14 18:39
 */

@Slf4j
@Service
public class ProblemServiceImpl extends AbstractBaseService<ProblemEntity, Long> implements ProblemService {

    private final ProblemDescRepository descRepository;
    private final SolutionRepository solutionRepository;

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
            buffer.setIsAccepted(System.currentTimeMillis() % 3 == 1);
            buffer.setLastSubmit(TimeUtil.now());
            buffer.setSubmitCount(0L);
            buffer.setSubmitPeopleCount(0L);
            listVOList.add(buffer);
        }
        return new PageImpl<>(listVOList, pageable, problemEntities.getTotalElements());
    }

    @Override
    public UserCenterVO fetchUserCenterProblem(Long userId) {
        List<SolutionEntity> list = solutionRepository.findAllByOwnerId(userId);
        List<Long> accepted = new LinkedList<>();
        List<Long> unsolved = new LinkedList<>();
        Map<SolutionResult, Long> resultMap = new TreeMap<>();

        HashMap<Long, SolutionResult> status = new HashMap<>();
        list.forEach(item -> {
            SolutionResult buffer;
            resultMap.put(item.getResult(), resultMap.getOrDefault(item.getResult(), 0L) + 1);
            if ((buffer = status.get(item.getProblemId())) == null || buffer != SolutionResult.ACCEPT) {
                status.put(item.getProblemId(), item.getResult());
            }
        });
        status.forEach((k,v) -> {
            if (v == SolutionResult.ACCEPT) {
                accepted.add(k);
            } else {
                unsolved.add(k);
            }
        });
        return new UserCenterVO(accepted, unsolved, resultMap);
    }
    @Override
    public void generatorProblem(ProblemParam param) {
        create(param.transformToEntity());
        descRepository.saveAndFlush(param.transFormToDescEntity());
    }



    protected ProblemServiceImpl(ProblemDescRepository descRepository,
                                 ProblemRepository repository,
                                 SolutionRepository solutionRepository) {
        super(repository);
        this.descRepository = descRepository;
        this.solutionRepository = solutionRepository;
    }
}
