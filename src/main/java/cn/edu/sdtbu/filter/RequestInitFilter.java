package cn.edu.sdtbu.filter;

import cn.edu.sdtbu.exception.ForbiddenException;
import cn.edu.sdtbu.exception.NotFoundException;
import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
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
 * used to persistent login & add some necessary header {@link OnlineJudgeConstant#REQUEST_ID} and {@link OnlineJudgeConstant#REQUEST_START_TIMESTAMP}
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
                    try {
                        userEntity = userService.login(cookie.getValue(), RequestUtil.getClientIp(request));
                        request.getSession().setAttribute(WebContextConstant.USER_SESSION_INFO, userEntity);
                        log.debug("user {} login by cookie", userEntity.getUsername());
                    } catch (ForbiddenException | NotFoundException e) {
                        ((HttpServletResponse) response).addCookie(WebContextConstant.EMPTY_REMEMBER_TOKEN);
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }
}
