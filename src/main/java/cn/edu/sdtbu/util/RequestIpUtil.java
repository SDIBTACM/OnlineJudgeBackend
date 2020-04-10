package cn.edu.sdtbu.util;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * I don't know why, but it can work
 *
 * @author Boxjan
 * @version 1.0
 * @date 2020-04-11 00:19
 */
public class RequestIpUtil {

    @Value("${proxy.trust-ips}")
    static private String TrustProxiesIpStr;

    static private String[] TrustProxiesIps;
    static {
        TrustProxiesIps = TrustProxiesIpStr.trim().split(" *, *");
    }

    /**
    Try to get the remote addr even server behind a proxy
     */
    static public String[] getClientIps(HttpServletRequest request) {
        ArrayList<String> ips = new ArrayList<>();

        ArrayList<String> forwardedIpsStrs = new ArrayList<>(
            Arrays.asList(request.getHeader("x-forwarded-for").trim().split(" *, *")));
        forwardedIpsStrs.add(request.getRemoteAddr());
        int i = forwardedIpsStrs.size() - 1;
        for (; i >= 0; i--) {
            if (!IpUtil.isIpInSubnets(forwardedIpsStrs.get(i), TrustProxiesIps)) {
                break;
            }
        }
        for (; i >= 0; i--) {
            ips.add(forwardedIpsStrs.get(i));
        }
        return ips.toArray(String[]::new);
    }

    static public String getClientIp(HttpServletRequest request) {
        return getClientIps(request)[0];
    }
}
