package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.ProblemEntity;
import cn.edu.sdtbu.model.param.ProblemParam;
import cn.edu.sdtbu.repository.ProblemDescRepository;
import cn.edu.sdtbu.repository.ProblemRepository;
import cn.edu.sdtbu.service.ProblemService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-14 18:39
 */

@Slf4j
@Service
public class ProblemServiceImpl extends AbstractBaseService<ProblemEntity, Long> implements ProblemService {

    private final ProblemDescRepository descRepository;

    protected ProblemServiceImpl(ProblemDescRepository descRepository, ProblemRepository repository) {
        super(repository);
        this.descRepository = descRepository;
    }

    @Override
    public void generatorProblem(ProblemParam param) {
        create(param.transformToEntity());
        descRepository.saveAndFlush(param.transFormToDescEntity());
    }
}
