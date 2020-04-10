package cn.edu.sdtbu.handler;

import cn.edu.sdtbu.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * global exception handler
 * @author bestsort
 * @version 1.0
 * @date 2020-04-10 16:17
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * validator result handler
     * @param e result
     * @return response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        Map<String, String> errors = new HashMap<>(errorList.size());
        errorList.forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        log.debug("bad request: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleCustomExceptions(BaseException e) {
        log.debug("exception type: {}, http status: {}, message: {}", e.getClass(), e.getStatus(),e.getErrorData());
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
}
