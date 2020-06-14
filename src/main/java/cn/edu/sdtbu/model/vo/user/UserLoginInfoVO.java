package cn.edu.sdtbu.model.vo.user;

import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.vo.base.BaseUserVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-20 09:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginInfoVO extends BaseUserVO {
    String token;
    @ApiModelProperty(notes = "标注用户身份:student(default)/teacher/admin")
    UserRole role;

    public static UserLoginInfoVO fetchByUserEntity(UserEntity entity) {
        UserLoginInfoVO loginInfo = new UserLoginInfoVO();
        loginInfo.setId(entity.getId());
        loginInfo.setToken(entity.getRememberToken());
        loginInfo.setUsername(entity.getUsername());
        loginInfo.setRole(entity.getRole());
        loginInfo.setNickname(entity.getNickname());
        return loginInfo;
    }
}
