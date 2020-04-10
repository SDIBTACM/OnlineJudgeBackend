package cn.edu.sdtbu.model.ao;

import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.enums.UserStatus;

import javax.validation.constraints.NotBlank;


/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-10 18:08
 */
public class UserUpdateAO {
    long id;

    String userName;

    String nickname;

    String password;

    String school;

    String email;

    UserRole role;

    UserStatus status;
}
