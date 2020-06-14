package cn.edu.sdtbu.model.dto;

import lombok.Data;
import redis.clients.jedis.Tuple;

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

    public static UserRankListDTO converByTuple(Tuple tuple) {
        UserRankListDTO dto   = new UserRankListDTO();
        double          score = tuple.getScore();
        dto.setAcceptedCount((int) score);
        dto.setId(Long.parseLong(tuple.getElement()));
        dto.setSubmitCount((int) ((score - (int) score) * 1e6));
        return dto;
    }
}
