package cn.edu.sdtbu.exception;

import org.springframework.http.HttpStatus;

/**
 * I don't know why, but it can work
 *
 * @author Boxjan
 * @version 1.0
 * @date 2020-04-10 23:21
 */
public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
