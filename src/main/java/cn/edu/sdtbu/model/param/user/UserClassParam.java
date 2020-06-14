package cn.edu.sdtbu.model.param.user;

import cn.edu.sdtbu.validator.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-21 11:59
 */
@Data
public class UserClassParam {
    @ApiModelProperty(notes = "班级名")
    @NotBlank(groups = BeforeCreate.class)
    @NullOrNotBlank(groups = BeforeUpdate.class)
    String name;

    @ApiModelProperty(notes = "班级内用户的username, 也可后续添加")
    List<String> usernames;

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