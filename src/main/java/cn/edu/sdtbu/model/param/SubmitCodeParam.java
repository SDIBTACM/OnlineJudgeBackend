package cn.edu.sdtbu.model.param;

import cn.edu.sdtbu.model.enums.LangType;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 09:51
 */

@Data
public class SubmitCodeParam {
    LangType type;
    String code;
    Long problemId;
    Long contestId;
}
