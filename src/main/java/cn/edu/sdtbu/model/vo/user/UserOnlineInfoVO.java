package cn.edu.sdtbu.model.vo.user;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-16 18:52
 */

@Data
public class UserOnlineInfoVO {
    Long id;

    String username;

    String nickname;

    Timestamp loginTime;

    String ip;
}
