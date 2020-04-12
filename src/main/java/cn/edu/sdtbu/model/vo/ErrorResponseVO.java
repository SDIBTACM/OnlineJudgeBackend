package cn.edu.sdtbu.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-11 20:34
 */

@Builder
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseVO {
    Integer code;
    Object type;
    Object title;
    Object errors;
    Object message;
}