package cn.edu.sdtbu.exception;

import org.springframework.http.HttpStatus;

/**
 * something not found
 * @author bestsort
 * @version 1.0
 * @date 2020-4-6 20:54
 */
public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
