package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.problem.ProblemDescEntity;
import cn.edu.sdtbu.repository.ProblemDescRepository;
import cn.edu.sdtbu.service.ProblemDescService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-29 21:11
 */
@Slf4j
@Service
public class ProblemDescServiceImpl extends AbstractBaseService<ProblemDescEntity, Long>
    implements ProblemDescService {

    private final ProblemDescRepository repository;

    protected ProblemDescServiceImpl(ProblemDescRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
