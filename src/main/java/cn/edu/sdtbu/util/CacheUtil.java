package cn.edu.sdtbu.util;

import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.UserEntity;
import cn.edu.sdtbu.model.enums.KeyPrefix;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.util.Pair;

import java.util.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-02 14:23
 */
public class CacheUtil {
    public static final String SEPARATOR = "::";
    public static final String NOT_DEFINED_PREFIX = "not_defined_prefix";
    public static final String COUNT_PREFIX = "count";

    public static String userSubmitCountKey(Long userId, Long problemId) {
        List<Pair<Class<?>, Object>> list = new ArrayList<>(4);
        list.add(Pair.of(UserEntity.class, userId));
        list.add(Pair.of(ProblemEntity.class, problemId));
        return defaultKey(list, KeyPrefix.SUBMIT_PEOPLE_COUNT);
    }

    public static String countKey(Class<?> clazz, Object arg, KeyPrefix type) {
        return defaultKey(clazz, arg, type);
    }

    public static String countKey(Pair<Class<?>, Object> arg1, Pair<Class<?>, Object> arg2, KeyPrefix type) {
        List<Pair<Class<?>, Object>> list = new ArrayList<>(4);
        list.add(arg2);
        list.add(arg1);
        return defaultKey(list, type);
    }

    public static String defaultKey(Class<?> clazz, Object arg, Object prefix) {
        return prefix + SEPARATOR + clazz.getName() + SEPARATOR + arg;
    }

    public static String defaultKey(List<Pair<Class<?>, Object>> clazzArgsMap, Object prefix) {
        clazzArgsMap.sort(Comparator.comparing(pir -> pir.getFirst().getSimpleName()));
        StringBuilder builder = new StringBuilder(StringUtils.isEmpty(prefix.toString()) ? NOT_DEFINED_PREFIX : prefix.toString());
        clazzArgsMap.forEach(pair -> builder.append(SEPARATOR).append(pair.getFirst().getSimpleName()));
        clazzArgsMap.forEach(pair -> builder.append(SEPARATOR).append(pair.getSecond()));
        return builder.toString();
    }
}
