package cn.edu.sdtbu.model.vo;

import cn.edu.sdtbu.model.entity.UserEntity;
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
    public static UserLoginInfo fetchByUserEntity(UserEntity entity) {
        UserLoginInfo loginInfo = new UserLoginInfo();
        loginInfo.id = entity.getId();
        loginInfo.token = entity.getRememberToken();
        loginInfo.username = entity.getUsername();
        return loginInfo;
    }
}
