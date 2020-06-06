package cn.edu.sdtbu.model.vo.contest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-06 07:55
 */
@Data
public class StandingProblemNodeVO {
    Integer order;
    Long submitCount;

    @ApiModelProperty(notes = "mills")
    Long acAt;
}
