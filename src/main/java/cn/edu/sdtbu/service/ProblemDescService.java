package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.ProblemDescEntity;
import cn.edu.sdtbu.service.base.BaseService;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-29 21:11
 */
public interface ProblemDescService extends BaseService<ProblemDescEntity, Long> {
    ProblemDescEntity getProblemDesc(Long id);
}
