package cn.edu.sdtbu.model.vo;

import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.model.enums.LangType;
import cn.edu.sdtbu.model.vo.base.BaseUserVO;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-24 08:19
 */
@Data
public class SolutionListNode {
    Long        id;
    Long        problemId;
    LangType    lang;
    Integer     memoryUsed;
    Integer     timeUsed;
    JudgeResult result;
    Long        similarAt;
    Long        similarPercent;
    Integer     codeLength;
    BaseUserVO  userInfo;
    Timestamp   createAt;
}
