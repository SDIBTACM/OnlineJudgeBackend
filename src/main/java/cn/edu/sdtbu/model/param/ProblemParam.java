package cn.edu.sdtbu.model.param;

import cn.edu.sdtbu.aop.annotation.NullOrNotBlank;
import cn.edu.sdtbu.model.entity.ProblemDescEntity;
import cn.edu.sdtbu.model.entity.ProblemEntity;
import cn.edu.sdtbu.model.enums.ProblemType;
import cn.edu.sdtbu.util.SpringBeanUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-20 09:02
 */
@Data
public class ProblemParam {
    @NotNull
    String title;
    @NullOrNotBlank
    String source;
    @NotNull
    String description;
    @NotNull
    String input;
    @NotNull
    String output;
    @NotNull
    String sample;
    @NullOrNotBlank
    String hint;

    //TODO max or min value
    @ApiModelProperty(notes = "micro seconds")
    Integer timeLimit = 1000;
    //TODO max or min value
    @ApiModelProperty(notes = "kbytes")
    Integer memoryLimit = 1 << 10;
    Boolean isSpecialJudge = false;
    ProblemType type = ProblemType.NORMAL;
    Boolean hide = false;
    public ProblemEntity transformToEntity() {
        ProblemEntity entity = new ProblemEntity();
        SpringBeanUtil.cloneWithoutNullVal(this, entity);
        return entity;
    }
    public ProblemDescEntity transFormToDescEntity() {
        ProblemDescEntity entity = new ProblemDescEntity();
        SpringBeanUtil.cloneWithoutNullVal(this, entity);
        return entity;
    }
}