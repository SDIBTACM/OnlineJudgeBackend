package cn.edu.sdtbu.model.vo;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 18:17
 */

@AllArgsConstructor
public class ResultVO {
    HttpStatus httpStatus;
    String message;

    public static ResultVO OkOf(String message) {
        return new ResultVO(HttpStatus.OK, message);
    }
    public static ResultVO success() {
        return new ResultVO(HttpStatus.OK, "success");
    }
}
