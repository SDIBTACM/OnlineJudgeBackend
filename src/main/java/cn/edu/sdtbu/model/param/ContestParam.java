package cn.edu.sdtbu.model.param;

import cn.edu.sdtbu.model.enums.ContestPrivilege;
import cn.edu.sdtbu.model.enums.LangType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-15 15:06
 */
@Data
public class ContestParam {

    @ApiModelProperty(notes = "比赛名称")
    String name;

    String description;

    @ApiModelProperty(notes = "比赛开始时间")
    Long startAt;

    @ApiModelProperty(notes = "比赛结束时间")
    Long endBefore;

    @ApiModelProperty(notes = "默认为比赛结束，无需要可以不传此参数")
    Long lockRankAt;

    @ApiModelProperty(notes = "当 privilege = NEED_REGISTER 的时候需要此项")
    Long registerBegin;
    @ApiModelProperty(notes = "当 privilege = NEED_REGISTER 的时候需要此项")
    Long registerEnd;

    @ApiModelProperty(notes = "当 privilege = PROTECT 的时候需要此项")
    String password;

    @ApiModelProperty(notes = "比赛类型")
    ContestPrivilege privilege;

    @ApiModelProperty(notes = "允许访问比赛的班级id")
    List<Long> classIds;

    @ApiModelProperty(notes = "除允许班级外允许访问比赛的用户名")
    List<String> allowUsernames;

    @ApiModelProperty(notes = "允许使用的语言类型, 为空可使用所有")
    List<LangType> allowLang;
    @ApiModelProperty(notes = "允许访问比赛的ip地址")
    List<String> allowIps;
    @ApiModelProperty(notes = "禁止访问比赛的ip地址")
    List<String> denyIps;
    @ApiModelProperty(notes = "禁止访问比赛的用户(此list优先级最高)")
    List<String> denyUsernames;

    @ApiModelProperty(notes = "比赛中的题目ID")
    List<ContestProblemParam> problems;
}
