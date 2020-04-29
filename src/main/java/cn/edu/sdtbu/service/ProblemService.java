package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.ProblemEntity;
import cn.edu.sdtbu.model.param.ProblemParam;
import cn.edu.sdtbu.service.base.BaseService;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-14 17:29
 */
public interface ProblemService extends BaseService<ProblemEntity, Long> {


    void generatorProblem(ProblemParam param);
}
