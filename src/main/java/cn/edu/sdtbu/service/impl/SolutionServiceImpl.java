package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.repository.SolutionRepository;
import cn.edu.sdtbu.service.SolutionService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 20:29
 */
@Service
public class SolutionServiceImpl extends AbstractBaseService<SolutionEntity, Long> implements SolutionService {
    protected SolutionServiceImpl(SolutionRepository repository) {
        super(repository);
    }

    @Resource
    SolutionRepository solutionRepository;
}
