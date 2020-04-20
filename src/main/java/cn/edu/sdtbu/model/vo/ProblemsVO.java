package cn.edu.sdtbu.model.vo;

import cn.edu.sdtbu.model.entity.ProblemEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-16 11:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProblemsVO extends ProblemEntity {
    private Boolean accept;
}
