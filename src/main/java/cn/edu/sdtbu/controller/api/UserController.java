package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.model.param.UserRegisterParam;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
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
    public ResponseEntity<String> login(@NotBlank @RequestParam String userName,
                                        @NotBlank @RequestParam String password,
                                        @ApiIgnore HttpSession session,
                                        @ApiIgnore HttpServletResponse response){
        UserEntity userEntity = userService.login(userName, password);
        log.debug("{} is login", userEntity);
        session.setAttribute(Const.USER_SESSION_INFO, userEntity);
        response.addCookie(new Cookie(Const.ACCOUNT_TOKEN, userEntity.getRememberToken()));
        return ResponseEntity.ok("success");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@ApiIgnore HttpSession session,
                                         @ApiIgnore HttpServletResponse response){
        log.debug("{} is logout", session.getAttribute(Const.USER_SESSION_INFO));
        session.removeAttribute(Const.USER_SESSION_INFO);
        response.addCookie(Const.EMPTY_ACCOUNT_COOKIE);
        return ResponseEntity.ok("success");
    }

    @PutMapping
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterParam registerAO) {
        log.debug("registered: {}", registerAO.toString());
        userService.addUser(registerAO);
        return ResponseEntity.ok("registered");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> queryUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.queryUserById(userId));
    }
}
