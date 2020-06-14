package cn.edu.sdtbu.util;

import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.Inet4Address;
import java.net.Inet6Address;


/**
 * I don't know why, but it can work
 *
 * @author Boxjan
 * @version 1.0
 * @date 2020-04-11 00:45
 */

public class IpUtil {

    static final String IP_CUT    = "/";
    static final int    BYTE_SIZE = 8;

    /**
     * Checks if an IPv4 or IPv6 address is contained in the list of given IPs or subnets.
     *
     * @param ip      string
     * @param subnets ips
     * @return bool
     */
    static public boolean isIpInSubnets(String ip, String[] subnets) {
        for (String subnet : subnets) {
            if (isIpInSubnet(ip, subnet)) {
                return true;
            }
        }
        return false;
    }

    /**
     * CChecks if an IPv4 or IPv6 address is contained in the list of given IP or subnet.
     *
     * @param ip     string
     * @param subnet string
     * @return bool
     */
    static public boolean isIpInSubnet(String ip, String subnet) {
        if (!isIp(ip)) {
            return false;
        }

        String address;
        int    netmask;
        if (subnet.contains(IP_CUT)) {
            address = subnet.split(IP_CUT, 2)[0];
            try {
                netmask = Integer.parseInt(subnet.split(IP_CUT, 2)[1]);
            } catch (Exception e) {
                return false;
            }
        } else {
            address = subnet;
            netmask = isIpv4(ip) ? 32 : 128;
        }

        return isIpMatchSubnetWithMask(ip, address, netmask);
    }

    /**
     * Check if the ip is of IP
     *
     * @param ip string
     * @return bool
     */
    static public boolean isIp(String ip) {
        return InetAddressValidator.getInstance().isValid(ip);
    }

    /**
     * Check if the ip is of IPv4 type
     *
     * @param ip string
     * @return bool
     */
    static public boolean isIpv4(String ip) {
        return InetAddressValidator.getInstance().isValidInet4Address(ip);
    }

    /**
     * Check if the ip is of IPv6 type
     *
     * @param ip string
     * @return bool
     */
    static public boolean isIpv6(String ip) {
        return InetAddressValidator.getInstance().isValidInet6Address(ip);
    }

    static private boolean isIpMatchSubnetWithMask(String ip, String subnet, int mask) {
        if (mask < 0) {
            return false;
        }

        if (isIpv4(ip) && isIpv4(subnet)) {
            return mask <= 32 && isIpv4MatchSubnetWithMask(ip, subnet, mask);
        }

        if (isIpv6(ip) && isIpv6(subnet)) {
            return mask <= 128 && isIpv6MatchSubnetWithMask(ip, subnet, mask);
        }

        return false;
    }

    static private boolean isIpv4MatchSubnetWithMask(String ip, String subnet, int netmask) {
        return isBytesMatchBits(ipv4ToBytes(ip), ipv4ToBytes(subnet), netmask);

    }

    static private boolean isIpv6MatchSubnetWithMask(String ip, String subnet, int netmask) {
        return isBytesMatchBits(ipv6ToBytes(ip), ipv6ToBytes(subnet), netmask);
    }

    static private boolean isBytesMatchBits(byte[] source, byte[] test, int bit) {
        if (source.length == 0 || source.length != test.length) {
            return false;
        }

        for (int i = 0, ceil = (int) Math.ceil(bit / (double) BYTE_SIZE); i < ceil; ++i) {
            int left = bit - BYTE_SIZE * i;
            left = Math.min(left, BYTE_SIZE);
            int mask = ~(0xff >> left) & 0xff;
            if ((source[i] & mask) != (test[i] & mask)) {
                return false;
            }
        }
        return true;
    }

    static public byte[] ipv4ToBytes(String ip) {
        Inet4Address inet4Address;
        try {
            inet4Address = (Inet4Address) Inet4Address.getByName(ip);
        } catch (Exception e) {
            return new byte[0];
        }
        return inet4Address.getAddress();
    }

    static public byte[] ipv6ToBytes(String ip) {
        Inet6Address inet6Address;
        try {
            inet6Address = (Inet6Address) Inet6Address.getByName(ip);
        } catch (Exception e) {
            return new byte[0];
        }
        return inet6Address.getAddress();
    }

}

