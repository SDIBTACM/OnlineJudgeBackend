package cn.edu.sdtbu.model.vo;

import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-20 09:07
 */
@Data
public class UserLoginInfo {
    Long id;
    String username;
    String token;
    @ApiModelProperty(notes = "标注用户身份:student(default)/teacher/admin")
    UserRole role;
    public static UserLoginInfo fetchByUserEntity(UserEntity entity) {
        UserLoginInfo loginInfo = new UserLoginInfo();
        loginInfo.id = entity.getId();
        loginInfo.token = entity.getRememberToken();
        loginInfo.username = entity.getUsername();
        loginInfo.role = entity.getRole();
        return loginInfo;
    }
}
