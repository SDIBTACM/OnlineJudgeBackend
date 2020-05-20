package cn.edu.sdtbu.util;

import cn.edu.sdtbu.model.entity.problem.ProblemEntity;
import cn.edu.sdtbu.model.entity.user.UserEntity;
import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.model.enums.KeyPrefix;
import cn.edu.sdtbu.model.properties.Const;
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
    private static final double RATIO = 1e-6;

    public static Double rankListScore(int acceptedCount, int submitCount) {
        return acceptedCount + (1 - submitCount * RATIO);
    }

    public static String userSubmitCountKey(Long userId, Long problemId) {
        List<Pair<Class<?>, String>> list = new ArrayList<>(4);
        list.add(CacheUtil.pair(UserEntity.class, userId));
        list.add(CacheUtil.pair(ProblemEntity.class, problemId));
        return defaultKey(list, KeyPrefix.USER_SUBMIT_COUNT);
    }


    public static String defaultKey(Pair<Class<?>, String> arg1, Pair<Class<?>, String> arg2, KeyPrefix type) {
        List<Pair<Class<?>, String>> list = new ArrayList<>(4);
        list.add(arg2);
        list.add(arg1);
        return defaultKey(list, type);
    }

    public static String defaultKey(Class<?> clazz, Object arg, Object prefix) {
        return prefix + SEPARATOR + clazz.getSimpleName() + SEPARATOR + arg;
    }

    public static String judgeResultCountKey(JudgeResult result, Long userId, boolean fetchAll) {
        return KeyPrefix.JUDGE_RESULT + SEPARATOR + userId + (fetchAll ? "" : (SEPARATOR + result));
    }
    public static String defaultKey(List<Pair<Class<?>, String>> clazzArgsMap, Object prefix) {
        clazzArgsMap.sort(Comparator.comparing(pir -> pir.getFirst().getSimpleName()));
        StringBuilder builder = new StringBuilder(StringUtils.isEmpty(prefix.toString()) ? NOT_DEFINED_PREFIX : prefix.toString());
        clazzArgsMap.forEach(pair -> builder.append(SEPARATOR).append(pair.getFirst().getSimpleName()));
        clazzArgsMap.forEach(pair -> builder.append(SEPARATOR).append(pair.getSecond()));
        return builder.toString();
    }
    public static <S, T> Pair<S, String> pair(S first, Object second) {
        if (second == null) {
            return Pair.of(first, Const.UNDEFINED);
        }
        return Pair.of(first, second.toString());
    }
}
