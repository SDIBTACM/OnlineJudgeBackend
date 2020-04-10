package util;

import cn.edu.sdtbu.util.IpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * I don't know why, but it can work
 *
 * @author Boxjan
 * @version 1.0
 * @date 2020-04-11 02:55
 */
@RunWith(Parameterized.class)
public class IpUnitTest {
    private boolean result;
    private String ip;
    private String[] subnets;

    public IpUnitTest(boolean result, String ip, String subnets) {
        this.ip = ip;
        this.subnets = subnets.trim().split(" *, *");
        this.result = result;
    }

    // creates the test data
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
            {true, "192.168.1.1", "192.168.1.1"},
            {true, "192.168.1.1", "192.168.1.1/1"},
            {true, "192.168.1.1", "192.168.1.0/24"},
            {false, "192.168.1.1", "1.2.3.4/1"},
            {false, "192.168.1.1", "192.168.1.1/33"}, // invalid subnet
            {true, "192.168.1.1", "1.2.3.4/1,  192.168.1.0/24"},
            {true, "192.168.1.1", " 192.168.1.0/24, 1.2.3.4/1"},
            {false, "192.168.1.1", "1.2.3.4/1, 4.3.2.1/1"},
            {true, "1.2.3.4", "0.0.0.0/0"},
            {true, "1.2.3.4", "192.168.1.0/0"},
            {false, "1.2.3.4", "256.256.256/0"}, // invalid CIDR notation
            {false, "an_invalid_ip", "192.168.1.0/24"},
            {true, "2a01:198:603:0:396e:4789:8e99:890f", "2a01:198:603:0::/65"},
            {false, "2a00:198:603:0:396e:4789:8e99:890f", "2a01:198:603:0::/65"},
            {false, "2a01:198:603:0:396e:4789:8e99:890f", "::1"},
            {true, "0:0:0:0:0:0:0:1", "::1"},
            {false, "0:0:603:0:396e:4789:8e99:0001", "::1"},
            {true, "0:0:603:0:396e:4789:8e99:0001", "::/0"},
            {true, "0:0:603:0:396e:4789:8e99:0001", "2a01:198:603:0::/0"},
            {true, "2a01:198:603:0:396e:4789:8e99:890f", "::1, 2a01:198:603:0::/65"},
            {true, "2a01:198:603:0:396e:4789:8e99:890f", " 2a01:198:603:0::/65, ::1"},
            {false, "2a01:198:603:0:396e:4789:8e99:890f", "::1, 1a01:198:603:0::/65"},
            {false, "}__test|O:21:&quot;JDatabaseDriverMysqli&quot;:3:{s:2", "::1"},
            {false, "2a01:198:603:0:396e:4789:8e99:890f", "unknown"},
        };
        return Arrays.asList(data);
    }


    @Test
    public void testMultiplyException() {
        assertEquals("Result", result, IpUtil.isIpInSubnets(ip, subnets));
    }

}
