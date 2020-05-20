package cn.edu.sdtbu.exception;

import org.springframework.http.HttpStatus;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-20 15:08
 */
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
