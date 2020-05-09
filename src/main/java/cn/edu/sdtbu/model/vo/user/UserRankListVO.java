package cn.edu.sdtbu.model.vo.user;

import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-08 08:52
 */
@Data
public class UserRankListVO {
    Long id;

    String username;

    String nickname;

    Integer acceptedCount;

    Integer submitCount;
}
