package cn.edu.sdtbu.model.vo.user;

import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 16:15
 */
@Data
public class UserSimpleInfoVO {
    String username;
    String nickname;
    String school;
    String email;
    Integer rank;
}
