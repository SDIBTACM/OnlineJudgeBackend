package cn.edu.sdtbu.service;

import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.ProblemParam;
import cn.edu.sdtbu.model.vo.ProblemDescVO;
import cn.edu.sdtbu.model.vo.ProblemSimpleListVO;
import cn.edu.sdtbu.model.vo.user.UserCenterVO;
import cn.edu.sdtbu.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-14 17:29
 */
public interface ProblemService extends BaseService<ProblemEntity, Long> {
    Page<ProblemSimpleListVO> listSimpleLists(UserEntity user, Pageable pageable);

    UserCenterVO fetchAllUserSubmitStatus(Long userId);


    void generatorProblem(ProblemParam param);

    ProblemDescVO getProblemDescVoById(ProblemDescVO vo, Long id, Long contestId, Long aLong);
}
