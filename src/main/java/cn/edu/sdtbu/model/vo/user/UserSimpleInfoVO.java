package cn.edu.sdtbu.model.vo.user;

import cn.edu.sdtbu.model.enums.UserRole;
import cn.edu.sdtbu.model.vo.base.BaseUserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 16:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSimpleInfoVO extends BaseUserVO {
    String   school;
    String   email;
    UserRole role;
    Long     rank;
}
