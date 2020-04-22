package cn.edu.sdtbu.model.param;

import cn.edu.sdtbu.aop.annotation.NullOrNotBlank;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.enums.UserStatus;
import cn.edu.sdtbu.util.SpringBeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-15 10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserParam {
    @NullOrNotBlank(groups = BeforeUpdate.class)
    @NotBlank(groups = BeforeResister.class)
    @Size(max = 128, groups = Default.class)
    private String username;


    @NullOrNotBlank(groups = BeforeUpdate.class)
    @NotBlank(groups = BeforeResister.class)
    @Size(max = 64, groups = Default.class)
    private String nickname;

    @NullOrNotBlank(groups = BeforeUpdate.class)
    @Size(min = 7, max = 255, groups = BeforeResister.class)
    private String password;

    @NullOrNotBlank(groups = BeforeUpdate.class)
    @NotBlank(groups = BeforeResister.class)
    @Size(max = 32, groups = Default.class)
    private String school;


    @Email(groups = Default.class)
    @Size(max = 128, groups = Default.class)
    private String email;

    @Null(groups = BeforeResister.class)
    @NullOrNotBlank(groups = BeforeUpdate.class)
    private UserRole role;

    @Null(groups = BeforeResister.class)
    @NullOrNotBlank(groups = BeforeUpdate.class)
    private UserStatus status;


    @GroupSequence({Default.class, BeforeResister.class})
    public interface Resister {

    }

    @GroupSequence({Default.class, BeforeUpdate.class})
    public interface Update {

    }

    public UserEntity transformToEntity() {
        return transformToEntity(new UserEntity());
    }
    public UserEntity transformToEntity(UserEntity entity) {
        SpringBeanUtil.cloneWithoutNullVal(this, entity);
        return entity;
    }

    public interface Default {

    }
    public interface BeforeResister {
    }
    public interface BeforeUpdate {

    }
}
