package cn.edu.sdtbu.handler;

import cn.edu.sdtbu.exception.BaseException;
import cn.edu.sdtbu.model.vo.ResponseVO;
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
    public ResponseEntity<ResponseVO> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        Map<String, String> errors = new HashMap<>(errorList.size());
        errorList.forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        log.debug("bad request: {}", errors);

        return ResponseEntity.badRequest().body(
                ResponseVO.builder()
                        .errors(errors)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("parameter cannot pass the check")
                        .build()
        );
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseVO> handleCustomExceptions(BaseException e) {
        log.debug("exception type: {}, http status: {}, message: {}", e.getClass(), e.getStatus(),e.getErrorData());
        return ResponseEntity.status(e.getStatus())
                .body(ResponseVO.builder()
                    .errors(e.getMessage())
                    .code(e.getStatus().value())
                    .build()
        );
    }
}
