package cn.edu.sdtbu.model.ao;

import cn.edu.sdtbu.model.entity.UserEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * user register model
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 15:30
 */
@Data
public class UserAO {
    private String userName;
    private String nickname;
    private String password;
    private String school;
    private String email;

    public UserEntity transformToEntity() {
        return transformToEntity(new UserEntity());
    }
    public UserEntity transformToEntity(UserEntity userEntity) {
        BeanUtils.copyProperties(this, userEntity);
        return userEntity;
    }
}
