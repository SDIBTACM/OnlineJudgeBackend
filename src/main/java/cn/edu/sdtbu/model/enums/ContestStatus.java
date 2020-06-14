package cn.edu.sdtbu.model.enums;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-15 14:37
 */
public enum ContestStatus {
    /**
     * 赛前
     */
    PENDING,
    /**
     * 赛时
     */
    RUNNING,
    /**
     * 赛后
     */
    ENDED,
    /**
     * 开放注册中(可注册)
     */
    REGISTERING,
    /**
     * 注册被驳回
     */
    REJECTED,
    /**
     * 已注册
     */
    REGISTERED
}
