package cn.edu.sdtbu.util.jvm;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-20 16:59
 */

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Properties;

/**
 * 类描述：JVM信息工具类
 *
 * @author ruipeng.lrp
 * @since 2017/10/23
 **/
public class JVMInfoUtils {
    static private RuntimeMXBean      RUNTIME     = ManagementFactory.getRuntimeMXBean();
    static private ClassLoadingMXBean CLASS_LOAD  = ManagementFactory.getClassLoadingMXBean();
    static private CompilationMXBean  COMPILATION = ManagementFactory.getCompilationMXBean();
    static private Properties         PROPERTIES  = System.getProperties();

    // JVM规范名称
    static public String getJVMSpecName() {
        return RUNTIME.getSpecName();
    }

    // JVM规范运营商
    static public String getJVMSpecVendor() {
        return RUNTIME.getSpecVendor();
    }

    // JVM规范版本
    static public String getJVMSpecVersion() {
        return RUNTIME.getSpecVersion();
    }

    // JVM名称
    static public String getJVMName() {
        return RUNTIME.getVmName();
    }

    // JVM运营商
    static public String getJVMVendor() {
        return RUNTIME.getVmVendor();
    }

    // JVM实现版本
    static public String getJVMVersion() {
        return RUNTIME.getVmVersion();
    }

    // JVM启动时间
    static public long getJVMStartTimeMs() {
        return RUNTIME.getStartTime();
    }

    // JVM运行时间
    static public long getJVMUpTimeMs() {
        return RUNTIME.getUptime();
    }

    // JVM当前加载类总量
    static public long getJVMLoadedClassCount() {
        return CLASS_LOAD.getLoadedClassCount();
    }

    // JVM已卸载类总量
    static public long getJVMUnLoadedClassCount() {
        return CLASS_LOAD.getUnloadedClassCount();
    }

    // JVM从启动到现在加载类总量
    static public long getJVMTotalLoadedClassCount() {
        return CLASS_LOAD.getTotalLoadedClassCount();
    }

    // JIT编译器名称
    static public String getJITName() {
        return COMPILATION.getName();
    }

    // JIT总编译时间
    static public long getJITTimeMs() {
        if (COMPILATION.isCompilationTimeMonitoringSupported()) {
            return COMPILATION.getTotalCompilationTime();
        }
        return -1;
    }

    // 获取指定key的属性值
    static public String getSystemProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    static public Properties getSystemProperty() {
        return PROPERTIES;
    }
}
