package cn.edu.sdtbu.manager;

import cn.edu.sdtbu.model.dto.ProblemExtraCodeDTO;
import org.springframework.stereotype.Component;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-21 09:23
 */
@Component
public interface JudgeMachineManager {
    ProblemExtraCodeDTO judgeProblem();
}
