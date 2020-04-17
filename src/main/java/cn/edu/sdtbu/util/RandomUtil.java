package cn.edu.sdtbu.util;

/**
 * get a register code
 * @author Soul
 * @version 1.0
 * @date 2020-04-15 20:21
 */

public class RandomUtil {
    public static String randomCode() {
        Integer res = (int)(Math.random() * 1000000);
        return res.toString();
    }
}