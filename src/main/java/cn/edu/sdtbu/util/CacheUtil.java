package cn.edu.sdtbu.util;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.TreeMap;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-02 14:23
 */
public class CacheUtil {
    public static final String SEPARATOR = "::";
    public static final String COUNT_PREFIX = "count";

    public static String countKey(Class<?> clazz, Object arg) {
        return COUNT_PREFIX + SEPARATOR + clazz.getSimpleName() + SEPARATOR + arg.toString();
    }

    public static String countKey(TreeMap<Class<?>, List<Object>> clazzArgsMap) {
        StringBuilder builder = new StringBuilder(COUNT_PREFIX);
        for (Class c : clazzArgsMap.keySet()) {
            builder.append(SEPARATOR)
                .append(c.getSimpleName());
        }
        for (Class c : clazzArgsMap.keySet()) {
            for (Object o : clazzArgsMap.get(c)) {
                builder.append(SEPARATOR)
                    .append(o.toString());
            }
        }
        return builder.toString();
    }

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
