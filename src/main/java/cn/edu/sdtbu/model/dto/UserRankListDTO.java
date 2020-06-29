package cn.edu.sdtbu.model.dto;

import cn.edu.sdtbu.util.CacheUtil;
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
        dto.setId(Long.parseLong(tuple.getElement()));
        CountPair countPair = CacheUtil.parseCountPair(tuple.getScore());
        dto.setAcceptedCount((int) countPair.accept);
        dto.setSubmitCount((int) countPair.submit);
        return dto;
    }
}
