package cn.edu.sdtbu.service.impl;

import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.entity.ProblemDescEntity;
import cn.edu.sdtbu.repository.ProblemDescRepository;
import cn.edu.sdtbu.service.ProblemDescService;
import cn.edu.sdtbu.service.base.AbstractBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public ProblemDescEntity getProblemDesc(Long id) {
        Optional<ProblemDescEntity> descEntity = repository.findById(id);
        return descEntity.orElseThrow(() ->
            new NotFoundException("this problem not found"));
    }
}
