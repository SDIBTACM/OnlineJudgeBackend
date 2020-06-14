package cn.edu.sdtbu.model.constant;

import com.google.common.net.HttpHeaders;

import javax.servlet.http.Cookie;
import java.util.concurrent.TimeUnit;

/**
 * 在 Web容器 上下文中传递数据时需要使用到的字段
 *
 * @author bestsort
 * @version 1.0
 * @date 2020-06-14 08:52
 */
public interface WebContextConstant {
    String REQUEST_COST_TIME       = "request-time-cost";
    String REQUEST_START_TIMESTAMP = "request_process_before";
    String SESSION_SET             = "session_set";

    String REQUEST_ID                  = "request_id";
    String USER_SESSION_INFO           = "user_session_info";
    String ACCOUNT_TOKEN               = "account_token";
    String REMEMBER_TOKEN              = "rememberToken";
    Cookie EMPTY_REMEMBER_TOKEN        = new Cookie(REMEMBER_TOKEN, null);
    long   REMEMBER_TOKEN_EXPRESS_TIME = TimeUnit.DAYS.toMillis(30);
    String USER_IP                     = "user_ip";
    String ADMIN_TOKEN_HEADER_NAME     = "ADMIN-" + HttpHeaders.AUTHORIZATION;
    String API_ACCESS_KEY_HEADER_NAME  = "API-" + HttpHeaders.AUTHORIZATION;

}
