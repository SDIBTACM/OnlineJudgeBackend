package cn.edu.sdtbu.interceptor;

import cn.edu.sdtbu.exception.ForbiddenException;
import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.RequestIpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-10 20:23
 */

@Slf4j
@Component
public class CookieInterceptor implements HandlerInterceptor {
    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserEntity userEntity = (UserEntity) request.getSession().getAttribute(Const.USER_SESSION_INFO);
        if (userEntity == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (Const.REMEMBER_TOKEN.equals(cookie.getName())) {
                    try {
                        userEntity = userService.login(cookie.getValue(), RequestIpUtil.getClientIp(request));
                        request.setAttribute(Const.USER_SESSION_INFO, userEntity);
                        log.debug("user {} login by cookie", userEntity.getUsername());
                    } catch (ForbiddenException ignore) {
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) { }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) { }
}
