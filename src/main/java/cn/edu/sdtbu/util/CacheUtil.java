package cn.edu.sdtbu.util;

import com.alibaba.fastjson.JSON;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-02 14:23
 */
public class CacheUtil {
    public static final String SEPARATOR = "::";
    public static String defaultKey(Class<?> clazz, Object... args) {
        StringBuilder builder = new StringBuilder(clazz.getSimpleName());
        for (Object arg : args) {
            builder.append(SEPARATOR).append(arg.toString());
        }
        return builder.toString();
    }
    public static Object parseDefault(Class<?> clazz, Object... args) {
        return JSON.parseObject(defaultKey(clazz, args), clazz);
    }
}
