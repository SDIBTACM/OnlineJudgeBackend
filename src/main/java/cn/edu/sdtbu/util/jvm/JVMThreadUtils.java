package cn.edu.sdtbu.util.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * 类描述：JVM 线程信息工具类
 *
 * @author ruipeng.lrp
 * @since 2017/10/23
 **/
public class JVMThreadUtils {
    static private final ThreadMXBean THREAD_M_X_BEAN;

    static {
        THREAD_M_X_BEAN = ManagementFactory.getThreadMXBean();
    }

    //Daemon线程总量
    static public int getDaemonThreadCount() {
        return THREAD_M_X_BEAN.getDaemonThreadCount();
    }

    //当前线程总量
    static public int getThreadCount() {
        return THREAD_M_X_BEAN.getThreadCount();
    }

    //死锁线程总量
    static public int getDeadLockedThreadCount() {
        try {
            long[] deadLockedThreadIds = THREAD_M_X_BEAN.findDeadlockedThreads();
            if (deadLockedThreadIds == null) {
                return 0;
            }
            return deadLockedThreadIds.length;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
