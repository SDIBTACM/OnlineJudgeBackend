package cn.edu.sdtbu.model.enums;

import cn.edu.sdtbu.config.IntEnumValueDeserializer;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-21 15:35
 */
@JSONType(deserializer = IntEnumValueDeserializer.class)
public enum JudgeResult implements IntValueEnum {
    REJUDGE_PENDING(-5),
    PENDING(-4),
    PREPARING(-3),
    COMPILING(-2),
    RUNNING(-1),

    ACCEPT(0),

    PRESENTATION_ERROR(1),
    WRONG_ANSWER(2),
    TIME_LIMIT_EXCEED(3),
    MEMORY_LIMIT_EXCEED(4),
    OUTPUT_LIMIT_EXCEED(5),
    RUNTIME_ERROR(6),
    COMPILE_ERROR(7),
    SYSTEM_ERROR(8),
    FAILED_OTHER(9);

    private final int value;
    @Override
    public int getValue() {
        return value;
    }
    JudgeResult(int value) {
        this.value = value;
    }
}
