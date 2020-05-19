package cn.edu.sdtbu.model.vo.contest;

import cn.edu.sdtbu.model.enums.ContestPrivilege;
import cn.edu.sdtbu.model.enums.ContestStatus;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(notes = "比赛名称")
    String name;

    ContestStatus status;

    ContestPrivilege privilege;

    @ApiModelProperty(notes = "该场比赛创建者的 username")
    String owner;

    @ApiModelProperty(notes = "比赛开始时间")
    Timestamp startAt;

    @ApiModelProperty(notes = "当前用户是否被允许参与该场比赛")
    Boolean allowed;
}
