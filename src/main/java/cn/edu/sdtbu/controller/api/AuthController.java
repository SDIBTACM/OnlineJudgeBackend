package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.exception.TeapotException;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.param.user.LoginParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.user.UserLoginInfoVO;
import cn.edu.sdtbu.service.LoginLogService;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * login | logout | reset password
 * @author bestsort
 * @version 1.0
 * @date 2020-04-12 07:18
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    @Resource
    UserService userService;
    @Resource
    LoginLogService loginLogService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginInfoVO> login(@RequestBody LoginParam loginParam,
                                                 @ApiIgnore HttpServletRequest request,
                                                 @ApiIgnore HttpServletResponse response) {
        UserEntity userEntity = userService.login(loginParam.getIdentify(), loginParam.getPassword(), RequestUtil.getClientIp(request));
        log.debug("{} is login", userEntity);
        request.getSession().setAttribute(Const.USER_SESSION_INFO, userEntity);
        if (loginParam.getRemember()) {
            Cookie rememberTokenCookie = new Cookie(Const.REMEMBER_TOKEN,
                userService.generateRememberToken(userEntity, RequestUtil.getClientIp(request)));
            rememberTokenCookie.setPath("/");
            response.addCookie(rememberTokenCookie);
        }
        return ResponseEntity.ok().body(UserLoginInfoVO.fetchByUserEntity(userEntity));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@ApiIgnore HttpServletRequest request,
                                       @ApiIgnore HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        UserEntity entity = (UserEntity) session.getAttribute(Const.USER_SESSION_INFO);
        if (entity == null) {
            throw new TeapotException("would you have a teapot?");
        }
        loginLogService.logout(entity.getId());
        session.invalidate();
        response.addCookie(Const.EMPTY_REMEMBER_TOKEN);
        return ResponseEntity.ok().build();
    }
}
