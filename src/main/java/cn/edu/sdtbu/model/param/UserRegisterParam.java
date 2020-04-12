package cn.edu.sdtbu.model.param;

import cn.edu.sdtbu.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * user register model
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 15:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterParam {
    @NotBlank
    @Size(max = 64)
    private String username;

    @NotBlank
    @Size(max = 64)
    private String nickname;

    @Size(min = 7, max = 255)
    private String password;

    @NotBlank
    @Size(max = 32)
    private String school;

    @Email
    @NotBlank
    @Size(max = 128)
    private String email;

    public UserEntity transformToEntity() {
        return transformToEntity(new UserEntity());
    }
    public UserEntity transformToEntity(UserEntity userEntity) {
        BeanUtils.copyProperties(this, userEntity);
        return userEntity;
    }
}
