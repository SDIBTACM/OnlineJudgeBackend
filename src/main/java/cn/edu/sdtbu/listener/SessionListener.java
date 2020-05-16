package cn.edu.sdtbu.listener;

import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.properties.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-16 07:21
 */

@Slf4j
@Component
@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent event) throws ClassCastException {
        HttpSession session = event.getSession();
        UserEntity userEntity = (UserEntity)session.getAttribute(Const.USER_SESSION_INFO);
        if (userEntity != null) {
            log.info("user [{}] logout, ", userEntity.getUsername());
        }
        ServletContext application = session.getServletContext();
        Set sessions = (Set) application.getAttribute(Const.SESSION_SET);
        // destroy session from set
        sessions.remove(session);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();

        ServletContext application = session.getServletContext();
        Set sessions = (Set) application.getAttribute(Const.SESSION_SET);
        if (sessions == null) {
            sessions = Collections.newSetFromMap(new ConcurrentHashMap<>(128));
            application.setAttribute(Const.SESSION_SET, sessions);
        }
        // add session to set
        sessions.add(session);
    }
}
