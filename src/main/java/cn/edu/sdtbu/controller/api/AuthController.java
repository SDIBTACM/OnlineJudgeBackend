package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.param.LoginParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.RequestIpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginParam loginParam,
                                        @ApiIgnore HttpServletRequest request,
                                        @ApiIgnore HttpServletResponse response) {
        UserEntity userEntity = userService.login(loginParam.getIdentify(), loginParam.getPassword(), RequestIpUtil.getClientIp(request));
        log.debug("{} is login", userEntity);
        request.getSession().setAttribute(Const.USER_SESSION_INFO, userEntity);
        if (loginParam.getRemember()) {
            response.addCookie(new Cookie(Const.REMEMBER_TOKEN,
                    userService.generateRememberToken(userEntity, RequestIpUtil.getClientIp(request))));
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@ApiIgnore HttpSession session,
                                             @ApiIgnore HttpServletResponse response) {
        log.debug("{} is logout", session.getAttribute(Const.USER_SESSION_INFO));
        session.removeAttribute(Const.USER_SESSION_INFO);
        response.addCookie(Const.EMPTY_REMEMBER_TOKEN);
        return ResponseEntity.ok("success");
    }
}
