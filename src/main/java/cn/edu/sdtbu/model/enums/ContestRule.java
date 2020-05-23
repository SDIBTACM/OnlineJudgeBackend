package cn.edu.sdtbu.model.enums;

import cn.edu.sdtbu.config.IntEnumValueDeserializer;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-20 08:09
 */
@JSONType(deserializer = IntEnumValueDeserializer.class)
public enum  ContestRule implements IntValueEnum {
    ACM(0),
    OI(1);
    private final int value;
    ContestRule(int value) {
        this.value = value;
    }
    @Override
    public int getValue() {
        return value;
    }
}
