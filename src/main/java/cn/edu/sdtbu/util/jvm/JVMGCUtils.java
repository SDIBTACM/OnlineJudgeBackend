package cn.edu.sdtbu.util.jvm;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

/**
 * 类描述：JVM GC信息工具类
 *
 * @author ruipeng.lrp
 * @since 2017/10/23
 **/
public class JVMGCUtils {
    static private GarbageCollectorMXBean FULL_GC;
    static private GarbageCollectorMXBean YOUNG_GC;

    static {
        for (GarbageCollectorMXBean item : ManagementFactory.getGarbageCollectorMXBeans()) {
            if ("ConcurrentMarkSweep".equals(item.getName())
                || "MarkSweepCompact".equals(item.getName())
                || "PS MarkSweep".equals(item.getName())
                || "G1 Old Generation".equals(item.getName())
                || "Garbage collection optimized for short pausetimes Old Collector".equals(item.getName())
                || "Garbage collection optimized for throughput Old Collector".equals(item.getName())
                || "Garbage collection optimized for deterministic pausetimes Old Collector".equals(item.getName())
            ) {
                FULL_GC = item;
            } else if ("ParNew".equals(item.getName())
                || "Copy".equals(item.getName())
                || "PS Scavenge".equals(item.getName())
                || "G1 Young Generation".equals(item.getName())
                || "Garbage collection optimized for short pausetimes Young Collector".equals(item.getName())
                || "Garbage collection optimized for throughput Young Collector".equals(item.getName())
                || "Garbage collection optimized for deterministic pausetimes Young Collector".equals(item.getName())
            ) {
                YOUNG_GC = item;
            }
        }
    }

    //YGC总次数
    static public long getYoungGCCollectionCount() {
        return YOUNG_GC == null ? 0 : YOUNG_GC.getCollectionCount();
    }

    //YGC总时间
    static public long getYoungGCCollectionTime() {
        return YOUNG_GC == null ? 0 : YOUNG_GC.getCollectionTime();
    }

    //FGC总次数
    public long getFullGCCollectionCount() {
        return FULL_GC == null ? 0 : FULL_GC.getCollectionCount();
    }

    //FGC总次数
    public long getFullGCCollectionTime() {
        return FULL_GC == null ? 0 : FULL_GC.getCollectionTime();
    }
}