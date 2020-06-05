package cn.edu.sdtbu.model.vo.contest;

import cn.edu.sdtbu.model.enums.ContestPrivilege;
import cn.edu.sdtbu.model.enums.ContestStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-15 14:50
 */

@Data
public class ContestDetailVO {
    String name;

    String description;

    Timestamp startAt;

    Timestamp endBefore;

    String owner;

    ContestPrivilege privilege;

    ContestStatus status;

    List<ContestProblemVO> problems;
}
