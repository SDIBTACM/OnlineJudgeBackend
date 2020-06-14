package cn.edu.sdtbu.util;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * I don't know why, but it worked
 *
 * @author bestsort
 * @version 1.0
 * @date 2020-04-28 12:28
 */
public class AopTargetUtil {

    /**
     * get target object
     *
     * @param proxy proxy object
     * @return target object
     * @throws Exception exception
     */
    public static Object getTarget(Object proxy) throws Exception {

        //is not proxy object
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }

        return AopUtils.isJdkDynamicProxy(proxy) ?
            getJdkDynamicProxyTargetObject(proxy) :
            getCglibProxyTargetObject(proxy);
    }


    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }


    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        return ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
    }
}
