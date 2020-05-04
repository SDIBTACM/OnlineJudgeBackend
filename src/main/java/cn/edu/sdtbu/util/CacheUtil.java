package cn.edu.sdtbu.util;

import cn.edu.sdtbu.model.entity.ProblemEntity;
import cn.edu.sdtbu.model.entity.UserEntity;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.SortedMap;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-02 14:23
 */
public class CacheUtil {
    public static final String SEPARATOR = "::";
    public static final String COUNT_PREFIX = "count";

    public static String countUserProblemKey(Long userId, Long problemId) {
        return countKey(UserEntity.class, userId) + SEPARATOR + defaultKey(ProblemEntity.class, problemId);
    }
    public static String countKey(Class<?> clazz, Object arg) {
        return defaultKey(clazz, arg, COUNT_PREFIX);
    }

    public static String countKey(SortedMap<Class<?>, List<Object>> clazzArgsMap) {
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

    public static String defaultKey(Class<?> clazz, Object args) {
        return defaultKey(clazz, args, null);
    }

    public static String defaultKey(Class<?> clazz, Object arg, String prefix) {
        StringBuilder builder = StringUtils.isEmpty(prefix) ?
            new StringBuilder() :
            new StringBuilder(prefix + SEPARATOR);
        builder.append(clazz.getSimpleName());
        builder.append(SEPARATOR).append(arg.toString());
        return builder.toString();
    }

    public static Object parseDefault(Class<?> clazz, Object args) {
        return JSON.parseObject(defaultKey(clazz, args), clazz);
    }
}
