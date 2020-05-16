package cn.edu.sdtbu.model.vo.user;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-16 21:09
 */
@Data
public class BannedLoginVO {
    String username;
    String bannedBy;
    Timestamp endTime;
}
