package cn.edu.sdtbu.model.vo.user;

import cn.edu.sdtbu.model.vo.base.BaseUserVO;
import lombok.Data;

import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-21 15:06
 */
@Data
public class UserClassesVO {
    Long id;

    String name;

    List<BaseUserVO> usersInfo;
}
