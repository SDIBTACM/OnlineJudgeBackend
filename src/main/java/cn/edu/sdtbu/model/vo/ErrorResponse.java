package cn.edu.sdtbu.model.vo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author bestsort
 */
@JsonInclude(Include.NON_NULL)
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final Integer code;
    private final Object type;
    private final Object errors;
    private final Object data;
    private final Object message;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
    public static ErrorResponse ok() {
        return ErrorResponse.builder()
                .ok()
                .build();
    }
    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public static class ResponseBuilder {
        private Integer code;
        private Object type;
        private Object errors;
        private Object message;
        private Object data;

        private ResponseBuilder() { }

        public ResponseBuilder code(final Integer code) {
            this.code = code;
            return this;
        }
        public ResponseBuilder message(final Object message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder code(final HttpStatus status) {
            this.code = status.value();
            return this;
        }

        public ResponseBuilder ok() {
            this.code = HttpStatus.OK.value();
            this.message = "success";
            return this;
        }

        public ResponseBuilder type(final Object type) {
            this.type = type;
            return this;
        }

        public ResponseBuilder errors(final Object errors) {
            this.errors = errors;
            return this;
        }

        public ResponseBuilder data(final Object message) {
            this.data = JSON.toJSONString(message);
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this.code, this.type, this.errors, this.data, this.message);
        }
    }
}
