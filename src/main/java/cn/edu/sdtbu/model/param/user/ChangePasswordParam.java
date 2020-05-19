package cn.edu.sdtbu.model.param.user;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 15:49
 */
@Data
public class ChangePasswordParam {
    @Size(min = 7, max = 255, groups = UserParam.BeforeResister.class)
    String oldPassword;
    @Size(min = 7, max = 255, groups = UserParam.BeforeResister.class)
    String newPassword;
}
