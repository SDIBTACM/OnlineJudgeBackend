package cn.edu.sdtbu.model.dto;

import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-08 08:59
 */
@Data
public class UserRankListDTO {
    Long id;

    Integer acceptedCount;

    Integer submitCount;
}
