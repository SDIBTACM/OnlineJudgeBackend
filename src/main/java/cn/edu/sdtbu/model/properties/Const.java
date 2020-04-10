package cn.edu.sdtbu.model.properties;

import javax.servlet.http.Cookie;
import java.sql.Timestamp;

/**
 * some const filed
 * @author bestsort
 * @version 1.0
 * @date 2020/4/6 下午8:36
 */
public class Const {
    /**
     * default time. used to replace NULL
     */
    public static final Timestamp TIME_ZERO = Timestamp.valueOf("1900-1-1 00:00:00");

    public static final String ONLINE_JUDGE_VERSION = "1.0";

    public static final String USER_SESSION_INFO = "user_session_info";
    public static final String ACCOUNT_TOKEN = "account_token";
    public static final Cookie EMPTY_ACCOUNT_COOKIE = new Cookie(ACCOUNT_TOKEN, "");

    // init
    static {
        EMPTY_ACCOUNT_COOKIE.setMaxAge(0);
        EMPTY_ACCOUNT_COOKIE.setPath("/");
    }
}
