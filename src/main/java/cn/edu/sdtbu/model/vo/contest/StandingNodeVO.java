package cn.edu.sdtbu.model.vo.contest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-06 07:52
 */
@Data
public class StandingNodeVO {
    Long userId;
    Integer rank;
    String username;
    String nickname;
    Integer solved;
    List<StandingProblemNodeVO> problemResults;
    @ApiModelProperty(notes = "mills")
    Long penalty;
}
