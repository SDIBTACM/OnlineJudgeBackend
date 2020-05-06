package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.solution.SolutionEntity;
import cn.edu.sdtbu.repository.SolutionRepository;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 20:29
 */
@Service
public class SolutionServiceImpl extends AbstractBaseService<SolutionEntity, Long> {
    protected SolutionServiceImpl(SolutionRepository repository) {
        super(repository);
    }
}
