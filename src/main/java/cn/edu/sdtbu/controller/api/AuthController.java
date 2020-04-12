package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.RequestIpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AuthController {
    @Resource
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@NotBlank @RequestParam String identify,
                                        @NotBlank @RequestParam String password,
                                        @RequestParam(defaultValue = "true") boolean remember,
                                        @ApiIgnore HttpServletRequest request,
                                        @ApiIgnore HttpServletResponse response) {
        UserEntity userEntity = userService.login(identify, password, RequestIpUtil.getClientIp(request));
        log.debug("{} is login", userEntity);
        request.getSession().setAttribute(Const.USER_SESSION_INFO, userEntity);
        if (remember) {
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
