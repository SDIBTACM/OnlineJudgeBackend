package cn.edu.sdtbu.model.vo.contest;

import cn.edu.sdtbu.model.enums.ContestPrivilege;
import cn.edu.sdtbu.model.enums.ContestStatus;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-15 14:34
 */
@Data
public class ContestsVO {
    Long id;

    String name;

    ContestStatus status;

    ContestPrivilege type;

    Timestamp startTime;
}
