package cn.edu.sdtbu.model.param.user;

import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-15 20:20
 */
@Data
public class LoginParam {
    private String  identify;
    private String  password;
    private Boolean remember = true;
}
