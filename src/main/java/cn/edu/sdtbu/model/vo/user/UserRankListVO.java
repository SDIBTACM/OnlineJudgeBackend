package cn.edu.sdtbu.model.vo.user;

import cn.edu.sdtbu.model.vo.base.BaseUserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-08 08:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRankListVO extends BaseUserVO {

    Integer acceptedCount;

    Integer submitCount;
}
