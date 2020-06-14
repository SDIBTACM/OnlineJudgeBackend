package cn.edu.sdtbu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * I don't know why, but it can work
 *
 * @author Boxjan
 * @version 1.0
 * @date 2020-04-17 12:47
 */
public class TeapotException extends BaseException {

    public TeapotException() {
        super(HttpStatus.I_AM_A_TEAPOT.getReasonPhrase());
    }

    public TeapotException(String message) {
        super(message);
    }

    public TeapotException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    @NonNull
    public HttpStatus getStatus() {
        return HttpStatus.I_AM_A_TEAPOT;
    }
}
