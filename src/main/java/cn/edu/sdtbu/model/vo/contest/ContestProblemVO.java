package cn.edu.sdtbu.model.vo.contest;

import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-15 14:49
 */
@Data
public class ContestProblemVO {
    Integer order;

    String title;

    Boolean isAccepted;

    Long acCount;

    Long submitCount;
}
