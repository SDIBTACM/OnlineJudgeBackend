package cn.edu.sdtbu.model.vo.base;

import cn.edu.sdtbu.model.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 16:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserVO {
    Long id;
    String username;
    String nickname;

    public static BaseUserVO fetchBaseFromEntity(UserEntity entity) {
        BaseUserVO baseUserVO = new BaseUserVO();
        baseUserVO.setId(entity.getId());
        baseUserVO.setNickname(entity.getNickname());
        baseUserVO.setUsername(entity.getUsername());
        return baseUserVO;
    }
}
