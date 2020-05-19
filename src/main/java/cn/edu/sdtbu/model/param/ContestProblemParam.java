package cn.edu.sdtbu.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 19:25
 */
@Data
public class ContestProblemParam {
    Long id;
    @ApiModelProperty(notes = "题目顺序, 越小越靠上")
    Integer order;

    @ApiModelProperty(notes = "默认为 id 所对应的name")
    String title;
}
