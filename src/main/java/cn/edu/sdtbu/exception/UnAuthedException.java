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
public class UnAuthedException extends BaseException {
    public UnAuthedException() {
        super(HttpStatus.FORBIDDEN.getReasonPhrase());
    }

    public UnAuthedException(String message) {
        super(message);
    }

    public UnAuthedException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
