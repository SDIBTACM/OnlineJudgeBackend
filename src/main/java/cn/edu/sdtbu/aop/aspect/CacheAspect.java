package cn.edu.sdtbu.aop.aspect;

import cn.edu.sdtbu.aop.annotation.Cache;
import cn.edu.sdtbu.aop.annotation.CacheDelete;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
    private static final String VOID = "void";
    @Resource
    CacheHandler handler;

    Random random = new Random();
    @Around("@annotation(cn.edu.sdtbu.aop.annotation.CacheDelete)")
    public Object aroundCacheDelete(ProceedingJoinPoint point) throws Throwable{
        CacheDelete cacheDelete = fetchAnnotation(point, CacheDelete.class);
        String key = fetchKey(point, cacheDelete.key(), cacheDelete.targetClass(), fetchMethod(point), cacheDelete.method());
        service().delete(key);
        Object object = point.proceed();
        Type type = fetchReturnType(point);
        if (!VOID.equals(type.getTypeName())) {
            return JSON.parseObject(object.toString(), type);
        }
        return null;
    }

    @Around("@annotation(cn.edu.sdtbu.aop.annotation.Cache)")
    public Object aroundCache(ProceedingJoinPoint point) throws Throwable {
        // fetch @Cache's args
        Cache cache = fetchAnnotation(point, Cache.class);
        // time transformation
        long timeOut = TimeUtil.time2Mill(cache.expire(), cache.timeUnit());
        if (cache.random()) {
            timeOut += TimeUtil.time2Mill(random.nextInt(cache.randomInterval()), cache.timeUnit());
        }
        // generator key
        String key = fetchKey(point, cache.key());

        String resStr;
        Object returnObj;
        // try fetch value from cache
        if (!StringUtils.isEmpty(resStr = service().get(key))) {
            Type type = fetchReturnType(point);
            if (VOID.equals(type.getTypeName())){
                point.proceed();
                return null;
            }
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

    /**
     * fetch return type(used to JSON's parse)
     * @param point join point
     * @return  return type
     */
    private Type fetchReturnType(ProceedingJoinPoint point) {
        Type returnType = ((MethodSignature)point.getSignature()).getReturnType();
        //if is abstract class implements method, need fetch actual type arguments(xxxEntity).
        return Modifier.isAbstract(point.getSignature().getDeclaringType().getModifiers()) &&
            //or return generic
            returnType.getTypeName().equals(Object.class.getName()) ?
            ((ParameterizedType)(point.getTarget().getClass().getGenericSuperclass())).getActualTypeArguments()[0] :
            returnType;
    }

    /**
     * fetch real impl method, not in abstract class
     * @param point joint point
     * @return  real method
     * @throws NoSuchMethodException not found such method in class impl
     */
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

    private String fetchKey(ProceedingJoinPoint point, String expression, Class<?> clazz, Method method) {
        return fetchKey(point, expression, clazz, method, method.getName());
    }

    /**
     * generator key(parsing SpEL and add prefix)。 eg.{Class}${Method}${args}
     * @param point join point
     * @param expression SpEL
     * @return  key
     * @throws NoSuchMethodException not found method
     */
    private String fetchKey(ProceedingJoinPoint point, String expression) throws NoSuchMethodException {
        return fetchKey(point, expression, point.getTarget().getClass(), fetchMethod(point));
    }

    private String fetchKey(ProceedingJoinPoint point, String expression, Class<?>clazz, Method method, String targetMethod){
        return clazz.getSimpleName() + SEPARATOR + targetMethod + SEPARATOR +
            SpringUtil.parseSpel(method, point.getArgs(), expression);
    }

    private CacheStore<String, String> service() {
        return handler.fetchCacheStore();
    }

    private <T extends Annotation> T fetchAnnotation(ProceedingJoinPoint point, Class<T> annotationClass){
        return ((MethodSignature)point.getSignature())
            .getMethod()
            .getAnnotation(annotationClass);
    }
}
