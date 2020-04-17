package cn.edu.sdtbu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * I don't know why, but it can work
 *
 * @author Boxjan
 * @version 1.0
 * @date 2020-04-10 23:21
 */
public class ForbiddenException extends BaseException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN.getReasonPhrase());
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
