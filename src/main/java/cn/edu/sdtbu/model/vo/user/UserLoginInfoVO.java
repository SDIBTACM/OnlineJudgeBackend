package cn.edu.sdtbu.model.vo.user;

import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-20 09:07
 */
@Data
public class UserLoginInfoVO {
    Long id;
    String username;
    String token;
    @ApiModelProperty(notes = "标注用户身份:student(default)/teacher/admin")
    UserRole role;
    public static UserLoginInfoVO fetchByUserEntity(UserEntity entity) {
        UserLoginInfoVO loginInfo = new UserLoginInfoVO();
        loginInfo.id = entity.getId();
        loginInfo.token = entity.getRememberToken();
        loginInfo.username = entity.getUsername();
        loginInfo.role = entity.getRole();
        return loginInfo;
    }
}
