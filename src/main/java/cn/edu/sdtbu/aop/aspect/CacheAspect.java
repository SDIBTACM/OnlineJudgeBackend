package cn.edu.sdtbu.aop.aspect;

import cn.edu.sdtbu.aop.annotation.Cache;
import cn.edu.sdtbu.cache.CacheStore;
import cn.edu.sdtbu.handler.CacheHandler;
import cn.edu.sdtbu.util.SpringUtil;
import cn.edu.sdtbu.util.TimeUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-23 22:18
 */
@Slf4j
@Aspect
@Component
public class CacheAspect {
    private static final String CACHE_HIT = "cache_hit";
    private static final String CACHE_MISS = "cache_miss";
    private static final String SEPARATOR = "$";

    @Resource
    CacheHandler handler;

    Random random = new Random();

    @Around("@annotation(cn.edu.sdtbu.aop.annotation.Cache)")
    public Object aroundCache(ProceedingJoinPoint point) throws Throwable {
        // fetch @Cache's args
        Cache cache = ((MethodSignature)point.getSignature())
            .getMethod()
            .getAnnotation(Cache.class);
        // time transformation
        long timeOut = TimeUtil.time2Mill(cache.expire(), cache.timeUnit());
        if (cache.random()) {
            timeOut += TimeUtil.time2Mill(random.nextInt(cache.randomInterval()), cache.timeUnit());
        }

        // generator key
        Method method = fetchMethod(point);
        String key =
            point.getTarget().getClass().getSimpleName() + SEPARATOR +
            method.getName() + SEPARATOR +
            SpringUtil.parseSpel(method, point.getArgs(), cache.key());

        String resStr;
        Object returnObj;
        // try fetch value from cache
        if (!StringUtils.isEmpty(resStr = service().get(key))) {
            Type type = fetchReturnType(point);
            try {
                returnObj = JSON.parseObject(resStr, type);
                service().inc(CACHE_HIT, 1);
                return returnObj;
            } catch (Throwable ignore) {
                log.error("cache string [{}] parse to object [{}] failed", resStr, type);
            }
        }
        // cache miss, fetch value from service()
        service().inc(CACHE_MISS, 1);
        returnObj = point.proceed();
        service().put(key, returnObj.toString(), timeOut, TimeUnit.MILLISECONDS);
        return returnObj;
    }

    private Type fetchReturnType(ProceedingJoinPoint point) {
        return ((ParameterizedType)(point.getTarget().getClass().getGenericSuperclass())).getActualTypeArguments()[0];
    }

    private Method fetchMethod(ProceedingJoinPoint point) throws NoSuchMethodException {
        Signature signature = point.getSignature();
        MethodSignature methodSignature;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("this annotation just used to method");
        }
        methodSignature = (MethodSignature) signature;
        Object target = point.getTarget();
        return target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

    private CacheStore<String, String> service() {
        return handler.fetchCacheStore();
    }
}
