package cn.edu.sdtbu.filter;

import cn.edu.sdtbu.model.constant.WebContextConstant;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.service.UserService;
import cn.edu.sdtbu.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求时添加必要信息：
 *
 * @author bestsort
 * @version 1.0
 * @date 2020-05-04 18:30
 */
@WebFilter
@Component
@Slf4j
public class RequestInitFilter implements Filter {
    @Resource
    UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request    = (HttpServletRequest) servletRequest;
        UserEntity         userEntity = (UserEntity) request.getSession().getAttribute(WebContextConstant.USER_SESSION_INFO);
        request.getSession().setAttribute(WebContextConstant.USER_IP, RequestUtil.getClientIp(request));
        // add necessary attribute
        request.setAttribute(WebContextConstant.REQUEST_START_TIMESTAMP, System.nanoTime());
        // add user info
        if (userEntity == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (WebContextConstant.REMEMBER_TOKEN.equals(cookie.getName())) {
                    userEntity = userService.login(cookie.getValue(), RequestUtil.getClientIp(request));
                    if (userEntity == null) {
                        ((HttpServletResponse) response).addCookie(WebContextConstant.EMPTY_REMEMBER_TOKEN);
                        break;
                    }
                    request.getSession().setAttribute(WebContextConstant.USER_SESSION_INFO, userEntity);
                    log.debug("user {} login by cookie", userEntity.getUsername());
                    break;
                }
            }
        }
        chain.doFilter(request, response);
    }
}
