package cn.edu.sdtbu.model.vo;

import cn.edu.sdtbu.model.enums.SolutionResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 15:47
 */
@Data
public class UserCenterVO {
    UserSimpleInfoVO userInfo;

    Timestamp lastLogin;

    @ApiModelProperty(notes = "已 accept 的 problem id")
    Collection<Long> accepted;

    @ApiModelProperty(notes = "提交后未 accept 的 problem id")
    Collection<Long> unsolved;

    @ApiModelProperty(notes = "Key-Value, 记录各个提交状态对应的总数")
    Map<SolutionResult, Long> resultMap;

    public UserCenterVO(Collection<Long> accepted, Collection<Long> unsolved, Map<SolutionResult, Long> map) {
        this.accepted = accepted;
        this.unsolved = unsolved;
        this.resultMap = map;
    }
}
