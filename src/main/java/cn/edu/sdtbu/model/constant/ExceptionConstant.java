package cn.edu.sdtbu.model.constant;

import cn.edu.sdtbu.exception.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-14 08:16
 */
public interface ExceptionConstant {
    ForbiddenException     FORBIDDEN      = new ForbiddenException();
    TeapotException        TEAPOT         = new TeapotException();
    BadRequestException    BAD_REQUEST    = new BadRequestException();
    NotAcceptableException NOT_ACCEPTABLE = new NotAcceptableException();
    NotFoundException      NOT_FOUND      = new NotFoundException();
    ExistException         EXISTED        = new ExistException();
    UnauthorizedException  UNAUTHORIZED   = new UnauthorizedException();
}
