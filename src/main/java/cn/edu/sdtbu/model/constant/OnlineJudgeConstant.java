package cn.edu.sdtbu.model.constant;

import java.sql.Timestamp;

/**
 * some const filed
 *
 * @author bestsort
 * @version 1.0
 * @date 2020/4/6 下午8:36
 */
public interface OnlineJudgeConstant {

    /**
     * 默认时间, 用以替换数据库中的 null 值
     */
    Timestamp TIME_ZERO = Timestamp.valueOf("1900-1-1 00:00:00");

    /**
     * 版本号
     */
    String ONLINE_JUDGE_VERSION = "1.0";


}
