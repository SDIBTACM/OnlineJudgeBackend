package cn.edu.sdtbu.controller.api;

import cn.edu.sdtbu.exception.ForbiddenException;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.KeyPrefix;
import cn.edu.sdtbu.model.param.LoginParam;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.model.vo.user.UserLoginInfoVO;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.CacheUtil;
import cn.edu.sdtbu.util.RequestIpUtil;
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
    CacheHandler handler;

    @PostMapping("/login")
    public ResponseEntity<UserLoginInfoVO> login(@RequestBody LoginParam loginParam,
                                                 @ApiIgnore HttpServletRequest request,
                                                 @ApiIgnore HttpServletResponse response) {
        String key = CacheUtil.defaultKey(String.class, loginParam.getIdentify(), KeyPrefix.BANED_USER);
        String banedBy = handler.fetchCacheStore().get(key);
        if (banedBy != null) {
            long ttl = handler.fetchCacheStore().ttl(key);
            throw new ForbiddenException(
                String.format(
                    "你已被 [%s] 老师禁止登录, 距离解冻还有 %d小时 %d分钟 %d秒",
                    banedBy, ttl / 3600, ttl % 3600 / 60, ttl % 60));
        }

        UserEntity userEntity = userService.login(loginParam.getIdentify(), loginParam.getPassword(), RequestIpUtil.getClientIp(request));
        log.debug("{} is login", userEntity);
        request.getSession().setAttribute(Const.USER_SESSION_INFO, userEntity);
        if (loginParam.getRemember()) {
            Cookie rememberTokenCookie = new Cookie(Const.REMEMBER_TOKEN,
                userService.generateRememberToken(userEntity, RequestIpUtil.getClientIp(request)));
            rememberTokenCookie.setPath("/");
            response.addCookie(rememberTokenCookie);
        }
        return ResponseEntity.ok().body(UserLoginInfoVO.fetchByUserEntity(userEntity));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@ApiIgnore HttpSession session,
                                             @ApiIgnore HttpServletResponse response) {
        session.invalidate();
        response.addCookie(Const.EMPTY_REMEMBER_TOKEN);
        return ResponseEntity.ok().build();
    }
}
