package cn.edu.sdtbu.model.dto;

import cn.edu.sdtbu.util.CacheUtil;
import lombok.Data;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        double       score = tuple.getScore();
        final Double zero  = Double.parseDouble("0");
        dto.setId(Long.parseLong(tuple.getElement()));

        // ac数和提交数相等时(一道题都没错)
        if (zero.equals(score - (int)score)) {
            dto.setAcceptedCount((int) score);
            dto.setSubmitCount((int) score);
        } else {

            int accepted = (int)score + 1;
            BigDecimal bigDecimal = new BigDecimal((accepted - score) / CacheUtil.RATIO);
            int submitCount = bigDecimal.setScale(0, RoundingMode.HALF_UP).intValue();
            dto.setSubmitCount(submitCount);
            dto.setAcceptedCount(accepted);
        }
        return dto;
    }
}
