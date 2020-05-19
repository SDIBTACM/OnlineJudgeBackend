package cn.edu.sdtbu.model.param;

import cn.edu.sdtbu.model.enums.ContestPrivilege;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-15 15:06
 */
@Data
public class ContestParam {
    String name;
    Timestamp startAt;
    Timestamp endBefore;
    Timestamp lockRankAt;
    Timestamp registerBegin;
    Timestamp registerEnd;
    String password;

    ContestPrivilege privilege;
    //TODO
}
