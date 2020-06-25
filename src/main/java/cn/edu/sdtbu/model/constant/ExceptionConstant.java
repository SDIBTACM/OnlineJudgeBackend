package cn.edu.sdtbu.model.constant;

import cn.edu.sdtbu.exception.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-14 08:16
 */
public interface ExceptionConstant {


    TeapotException     TEAPOT      = new TeapotException();


    BadRequestException NOT_ACTIVE  = new BadRequestException("用户已注册！请检查是否已激活账户，若激活邮箱设置错误可等15分钟后重新注册");

    NotFoundException      NOT_FOUND                = new NotFoundException();


    ExistException         REGISTER_ACCOUNT_EXISTED = new ExistException("该账户中的username/email已被使用。若此账号还未激活，请30分钟后重新注册.");


    UnauthorizedException  UNAUTHORIZED             = new UnauthorizedException();
    UnauthorizedException  NEED_LOGIN               = new UnauthorizedException("请先登录");
    UnauthorizedException  NO_PERMISSION            = new UnauthorizedException("无权限，若未登录请尝试登录后再执行此操作");

    ForbiddenException REPEAT_SUBMIT = new ForbiddenException("重复提交相同代码");
}
