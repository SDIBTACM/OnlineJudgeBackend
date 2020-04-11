package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.param.UserRegisterParam;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.RequestIpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 16:06
 */

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    UserService userService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> updateUserById(
            @RequestBody UserEntity userEntity,
            @PathVariable Long userId) {
        userEntity.setId(userId);
        userService.updateUser(userEntity);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@NotBlank @RequestParam String username,
                                        @NotBlank @RequestParam String password,
                                        @RequestParam(defaultValue = "true") boolean remember,
                                        @ApiIgnore HttpServletRequest request,
                                        @ApiIgnore HttpServletResponse response) {
        UserEntity userEntity = userService.login(username, password);
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

    @PutMapping
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterParam registerAo) {
        log.debug("registered: {}", registerAo.toString());
        userService.addUser(registerAo);
        return ResponseEntity.ok("registered");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> queryUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.queryUserById(userId));
    }
}
