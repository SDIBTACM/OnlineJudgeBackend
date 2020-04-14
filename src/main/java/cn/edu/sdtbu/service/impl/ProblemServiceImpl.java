package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.model.entity.ProblemEntity;
import cn.edu.sdtbu.repository.ProblemRepository;
import cn.edu.sdtbu.service.ProblemService;
import cn.edu.sdtbu.service.base.AbstractBaseService;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-14 18:39
 */
public class ProblemServiceImpl extends AbstractBaseService<ProblemEntity, Long> implements ProblemService {

    private final ProblemRepository repository;

    protected ProblemServiceImpl(ProblemRepository repository) {
        super(repository);
        this.repository = repository;
    }

}
