package cn.edu.sdtbu.model.param;

import cn.edu.sdtbu.model.enums.LangType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 09:51
 */

@Data
public class SubmitCodeParam {
    @ApiModelProperty(notes = "所提交的语言类型")
    LangType type;
    @ApiModelProperty(notes = "代码")
    String   code;
    String md5;
    Long problemId;
    Long contestId;
}
