package cn.edu.sdtbu.model.vo;

import cn.edu.sdtbu.model.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-20 09:07
 */
@Builder
@Getter
public class UserLoginInfo {
    Long id;
    String username;
    String token;
    public static UserLoginInfo fetchByUserEntity(UserEntity entity) {
        return UserLoginInfo.builder()
            .id(entity.getId())
            .token(entity.getRememberToken())
            .username(entity.getUsername()).build();
    }
}
