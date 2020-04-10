package cn.edu.sdtbu.interceptor;

import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.properties.Const;
import cn.edu.sdtbu.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
public class SessionInterceptor implements HandlerInterceptor {
    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserEntity userEntity = (UserEntity) request.getSession().getAttribute(Const.USER_SESSION_INFO);
        if (userEntity == null && request.getCookies() != null){
            for (Cookie cookie : request.getCookies()){
                if (Const.ACCOUNT_TOKEN.equals(cookie.getName())){
                    try {
                        userEntity = userService.login(cookie.getValue());
                        request.setAttribute(Const.USER_SESSION_INFO, userEntity);
                    } catch (NotFoundException ignore) {}
                }
            }
        }
        log.debug("user {} login by cookie", userEntity);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) { }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) { }
}
