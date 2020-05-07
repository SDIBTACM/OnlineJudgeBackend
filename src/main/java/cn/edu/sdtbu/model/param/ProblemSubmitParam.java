package cn.edu.sdtbu.model.param;

import cn.edu.sdtbu.model.enums.LangType;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 08:44
 */
@Data
public class ProblemSubmitParam {
    LangType language;

    Long questionId;

    String code;
}
