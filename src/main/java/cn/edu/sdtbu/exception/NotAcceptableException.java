package cn.edu.sdtbu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-10 20:18
 */
public class NotAcceptableException extends BaseException {
    public NotAcceptableException() {
        super(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase());
    }

    public NotAcceptableException(String message) {
        super(message);
    }

    public NotAcceptableException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_ACCEPTABLE;
    }
}
