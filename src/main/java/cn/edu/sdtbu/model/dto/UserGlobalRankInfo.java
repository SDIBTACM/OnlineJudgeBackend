package cn.edu.sdtbu.model.dto;

import lombok.Data;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-07 14:35
 */
@Data
public class UserGlobalRankInfo {
    Long userId;

    Integer submitCount;

    Integer acceptCount;

    String nickname;
}
