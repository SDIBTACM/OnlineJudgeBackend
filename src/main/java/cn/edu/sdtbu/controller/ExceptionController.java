package cn.edu.sdtbu.controller;

import cn.edu.sdtbu.exception.BaseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * TODO
 *
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 16:54
 */

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    void handle(Throwable e,
                HttpServletRequest request,
                HttpServletResponse response) {
        String contentType = request.getContentType();
        String type = "application/json";

        // get exception info
        if (type.equals(contentType) && e instanceof BaseException) {
            try {
                response.setContentType(type);
                response.setStatus(((BaseException) e).getStatus().value());
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(e.getMessage());
                writer.close();
            } catch (IOException ignored) {
                // ignore
            }
        }
    }
}
