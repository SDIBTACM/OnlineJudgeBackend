package cn.edu.sdtbu.model.param;

import cn.edu.sdtbu.model.enums.ContestPrivilege;
import cn.edu.sdtbu.model.enums.ContestRule;
import cn.edu.sdtbu.model.enums.LangType;
import cn.edu.sdtbu.validator.annotation.FutureMills;
import cn.edu.sdtbu.validator.annotation.NullOrNotBlank;
import cn.edu.sdtbu.validator.annotation.NullableFutureMills;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-15 15:06
 */
@Data
public class ContestParam {

    @NotBlank(groups = BeforeCreate.class)
    @NullOrNotBlank(groups = BeforeUpdate.class)
    @Size(max = 255, groups = Default.class)
    @ApiModelProperty(notes = "必须, 比赛名称")
    String name;

    @NullOrNotBlank(groups = BeforeUpdate.class)
    @NotNull(groups = BeforeCreate.class)
    @ApiModelProperty(notes = "必须, 赛制, ACM / OI")
    ContestRule contestRule;

    String description;

    @FutureMills(groups = BeforeCreate.class)
    @NullableFutureMills(groups = BeforeUpdate.class)
    @ApiModelProperty(notes = "必须,比赛开始时间(ms)")
    Long startAt;

    @FutureMills(groups = BeforeCreate.class)
    @NullableFutureMills(groups = BeforeUpdate.class)
    @ApiModelProperty(notes = "必须, 比赛结束时间(ms)")
    Long endBefore;

    @ApiModelProperty(notes = "非必须,默认为比赛结束")
    Long lockRankAt;

    @NullableFutureMills
    @ApiModelProperty(notes = "当 privilege = NEED_REGISTER 的时候需要此项")
    Long registerBegin;


    @NullableFutureMills
    @ApiModelProperty(notes = "当 privilege = NEED_REGISTER 的时候需要此项")
    Long registerEnd;

    @NullOrNotBlank(groups = Default.class)
    @ApiModelProperty(notes = "当 privilege = PROTECT 的时候需要此项")
    String password;

    @ApiModelProperty(notes = "必须,比赛类型")
    ContestPrivilege privilege;

    @ApiModelProperty(notes = "非必须,允许访问比赛的班级id")
    List<Long> classIds;

    @ApiModelProperty(notes = "非必须,除允许班级外允许访问比赛的用户名")
    List<String> allowUsernames;

    @ApiModelProperty(notes = "非必须,允许使用的语言类型, 为空可使用所有")
    List<LangType> allowLang;
    @ApiModelProperty(notes = "非必须,允许访问比赛的ip地址")
    List<String>   allowIps;
    @ApiModelProperty(notes = "非必须,禁止访问比赛的ip地址")
    List<String>   denyIps;
    @ApiModelProperty(notes = "非必须,禁止访问比赛的用户(此list优先级最高)")
    List<String>   denyUsernames;

    @NotEmpty(groups = BeforeCreate.class)
    @ApiModelProperty(notes = "必须,比赛中的题目ID")
    List<ContestProblemParam> problems;


    @GroupSequence({Default.class, BeforeCreate.class})
    public interface Create {
    }

    @GroupSequence({Default.class, BeforeUpdate.class})
    public interface Update {

    }


    public interface Default {

    }

    public interface BeforeCreate {
    }

    public interface BeforeUpdate {

    }
}
