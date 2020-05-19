package cn.edu.sdtbu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 16:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestPrivilegeInfoDTO {
    String password;
    Long registerBegin;
    Long registerEnd;
}
