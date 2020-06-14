package cn.edu.sdtbu.exception;

import org.springframework.http.HttpStatus;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 21:49
 */
public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
