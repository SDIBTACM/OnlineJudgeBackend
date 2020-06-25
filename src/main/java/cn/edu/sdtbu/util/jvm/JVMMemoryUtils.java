package cn.edu.sdtbu.util.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

/**
 * 类描述：JVM内存信息工具类
 *
 * @author ruipeng.lrp
 * @since 2017/10/23
 **/
public class JVMMemoryUtils {
    static private final MemoryMXBean     MEMORY_M_X_BEAN;
    static private       MemoryPoolMXBean PERM_GEN_MX_BEAN;
    static private MemoryPoolMXBean OLD_GEN_MX_BEAN;
    static private MemoryPoolMXBean EDEN_SPACE_MX_BEAN;
    static private MemoryPoolMXBean PS_SURVIVOR_SPACE_MX_BEAN;

    static {
        MEMORY_M_X_BEAN = ManagementFactory.getMemoryMXBean();
        List<MemoryPoolMXBean> list = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean item : list) {
            if ("CMS Perm Gen".equals(item.getName()) 
                || "Perm Gen".equals(item.getName()) 
                || "PS Perm Gen".equals(item.getName()) 
                || "G1 Perm Gen".equals(item.getName()) 
            ) {
                PERM_GEN_MX_BEAN = item;
            } else if ("CMS Old Gen".equals(item.getName()) 
                || "Tenured Gen".equals(item.getName()) 
                || "PS Old Gen".equals(item.getName()) 
                || "G1 Old Gen".equals(item.getName()) 
            ) {
                OLD_GEN_MX_BEAN = item;
            } else if ("Par Eden Space".equals(item.getName()) 
                || "Eden Space".equals(item.getName()) 
                || "PS Eden Space".equals(item.getName()) 
                || "G1 Eden".equals(item.getName()) 
            ) {
                EDEN_SPACE_MX_BEAN = item;
            } else if ("Par Survivor Space".equals(item.getName()) 
                || "Survivor Space".equals(item.getName()) 
                || "PS Survivor Space".equals(item.getName()) 
                || "G1 Survivor".equals(item.getName()) 
            ) {
                PS_SURVIVOR_SPACE_MX_BEAN = item;
            }
        }
    }

    /**
     * 堆内存-已使用
     */
    static public long getHeapMemoryUsed() {
        return MEMORY_M_X_BEAN.getHeapMemoryUsage().getUsed();
    }

    /**
     * 堆内存-最大值
     *
     */
    static public long getHeapMemoryMax() {
        return MEMORY_M_X_BEAN.getHeapMemoryUsage().getMax();
    }

    /**
     * 堆外内存-已使用
     *
     */
    static public long getNonHeapMemoryUsed() {
        return MEMORY_M_X_BEAN.getNonHeapMemoryUsage().getUsed();
    }

    /**
     * 堆外内存-最大值
     *
     */
    static public long getNonHeapMemoryMax() {
        return MEMORY_M_X_BEAN.getNonHeapMemoryUsage().getMax();
    }

    /**
     * 持久代-已使用
     *
     */
    static public long getPermGenUsed() {
        return null == PERM_GEN_MX_BEAN ? 0 : PERM_GEN_MX_BEAN.getUsage().getUsed();
    }

     //持久代-最大值
    static public long getPermGenMax() {
        return null == PERM_GEN_MX_BEAN ? 0 : PERM_GEN_MX_BEAN.getUsage().getMax();
    }

     //老年代-已使用
    static public long getOldGenUsed() {
        return null == OLD_GEN_MX_BEAN ? 0 : OLD_GEN_MX_BEAN.getUsage().getUsed();
    }

     //老年代-最大值
    static public long getOldGenMax() {
        return null == OLD_GEN_MX_BEAN ? 0 : OLD_GEN_MX_BEAN.getUsage().getMax();
    }

     //Eden-已使用
    static public long getEdenGenUsed() {
        return null == EDEN_SPACE_MX_BEAN ? 0 : EDEN_SPACE_MX_BEAN.getUsage().getUsed();
    }

     //Eden-最大值
    static public long getEdenGenMax() {
        return null == EDEN_SPACE_MX_BEAN ? 0 : EDEN_SPACE_MX_BEAN.getUsage().getMax();
    }

     //Survivor-已使用
    static public long getSurvivorUsed() {
        return null == PS_SURVIVOR_SPACE_MX_BEAN ? 0 : PS_SURVIVOR_SPACE_MX_BEAN.getUsage().getUsed();
    }

     //Survivor-最大值
    static public long getSurvivorMax() {
        return null == PS_SURVIVOR_SPACE_MX_BEAN ? 0 : PS_SURVIVOR_SPACE_MX_BEAN.getUsage().getMax();
    }

}
