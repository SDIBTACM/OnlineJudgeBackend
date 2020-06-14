package cn.edu.sdtbu.model.query;

import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.model.enums.LangType;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-25 11:21
 */
@Data
public class SolutionQuery {
    Long        ownerId;
    Long        problemId;
    Long        contestId;
    LangType    lang;
    JudgeResult result;
    Integer     timeUsed;
    Integer     memoryUsed;
    Long        similarAt;
    Long        similarPercent;
}
