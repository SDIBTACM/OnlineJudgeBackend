package cn.edu.sdtbu.model.properties;

import com.google.common.net.HttpHeaders;

import javax.servlet.http.Cookie;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * some const filed
 * @author bestsort
 * @version 1.0
 * @date 2020/4/6 下午8:36
 */
public class Const {
/***********************************         default time. used to replace NULL        ********************************/
    public static final Timestamp TIME_ZERO = Timestamp.valueOf("1900-1-1 00:00:00");

    public static final String ONLINE_JUDGE_VERSION = "1.0";

/***********************************         About Some Web Config Field         ********************************/
    public static final String REQUEST_COST_TIME = "request-time-cost";
    public static final String REQUEST_START_TIMESTAMP = "request_process_before";
    public static final String SESSION_SET = "session_set";

    public static final String REQUEST_ID = "request_id";
    public static final String USER_SESSION_INFO = "user_session_info";
    public static final String ACCOUNT_TOKEN = "account_token";
    public static final String REMEMBER_TOKEN = "rememberToken";
    public static final Cookie EMPTY_REMEMBER_TOKEN = new Cookie(REMEMBER_TOKEN, null);
    public static final long REMEMBER_TOKEN_EXPRESS_TIME = TimeUnit.DAYS.toMillis(30);
    public static final String USER_IP = "user_ip";
    public static final String UNDEFINED = "undefined";
    public static final String ADMIN_TOKEN_HEADER_NAME = "ADMIN-" + HttpHeaders.AUTHORIZATION;
    public final static String API_ACCESS_KEY_HEADER_NAME = "API-" + HttpHeaders.AUTHORIZATION;

    // init
    static {
        EMPTY_REMEMBER_TOKEN.setMaxAge(0);
        EMPTY_REMEMBER_TOKEN.setPath("/");
    }
}
