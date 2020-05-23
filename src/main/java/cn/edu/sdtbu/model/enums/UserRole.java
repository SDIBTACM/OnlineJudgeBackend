package cn.edu.sdtbu.model.enums;

import cn.edu.sdtbu.config.IntEnumValueDeserializer;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 15:44
 */
@JSONType(deserializer = IntEnumValueDeserializer.class)
public enum UserRole implements IntValueEnum {
    // locked, can't login
    LOCKED(-1),
    // default role
    STUDENT(1),
    // teacher, need admin change role
    TEACHER(2),
    // admin
    ADMIN(3);

    private final int value;
    @Override
    public int getValue() {
        return value;
    }
    UserRole(int value) {
        this.value = value;
    }
}
