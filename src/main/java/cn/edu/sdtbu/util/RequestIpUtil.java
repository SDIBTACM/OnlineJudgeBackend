package cn.edu.sdtbu.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * I don't know why, but it can work
 *
 * @author Boxjan
 * @version 1.0
 * @date 2020-04-11 00:19
 */
@Component
public class RequestIpUtil {

    static private String[] TrustProxiesIps;

    @Value("${spring.proxy.trust-ips}")
    public void setTrustProxiesIpStr(String ipStr) {
        TrustProxiesIps = ipStr.trim().split(" *, *");
    }

    /**
    Try to get the remote addr even server behind a proxy
     */
    static public String[] getClientIps(HttpServletRequest request) {
        ArrayList<String> forwardedIpsStrs;
        String xForwardedFor = request.getHeader("x-forwarded-for");

        if (xForwardedFor != null && xForwardedFor.trim().length() > 0) {
            forwardedIpsStrs = new ArrayList<>(
                Arrays.asList(xForwardedFor.trim().split(" *, *")));
        } else {
            forwardedIpsStrs =  new ArrayList<>();
        }

        forwardedIpsStrs.add(request.getRemoteAddr());

        return getClientIps(forwardedIpsStrs);

    }

    static public String[] getClientIps(ArrayList<String> forwarded) {
        ArrayList<String> ips = new ArrayList<>();
        int i = forwarded.size() - 1;
        for (; i >= 1; i--) {
            if (!IpUtil.isIpInSubnets(forwarded.get(i), TrustProxiesIps)) {
                break;
            }
        }
        for (; i >= 0; i--) {
            ips.add(forwarded.get(i));
        }
        return ips.toArray(String[]::new);
    }

    static public String getClientIp(HttpServletRequest request) {
        return getClientIps(request)[0];
    }
}
