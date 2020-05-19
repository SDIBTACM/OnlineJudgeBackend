package cn.edu.sdtbu.model.vo.user;

import cn.edu.sdtbu.model.vo.base.BaseUserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-16 18:52
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class UserOnlineInfoVO extends BaseUserVO {

    String email;

    Timestamp loginTime;

    String ip;
}
