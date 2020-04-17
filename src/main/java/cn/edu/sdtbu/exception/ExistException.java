package cn.edu.sdtbu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * something exist
 * @author bestsort
 * @version 1.0
 * @date 2020-04-10 19:16
 */
public class ExistException extends BaseException {
    public ExistException(String message) {
        super(message);
    }

    public ExistException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
