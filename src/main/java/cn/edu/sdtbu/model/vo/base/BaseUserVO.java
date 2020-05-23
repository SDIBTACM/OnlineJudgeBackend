package cn.edu.sdtbu.model.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 16:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserVO {
    Long id;
    String username;
    String nickname;
}
