package cn.edu.sdtbu.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 19:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestProblemParam {
    Long id;

    @ApiModelProperty(notes = "非必须,默认为 id 所对应的name")
    String title;
}
