package cn.edu.sdtbu.model.enums;

import cn.edu.sdtbu.config.IntEnumValueDeserializer;
import com.alibaba.fastjson.annotation.JSONType;
import org.springframework.util.Assert;

import java.util.stream.Stream;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-10 07:46
 */
@JSONType(deserializer = IntEnumValueDeserializer.class)
public interface IntValueEnum {

    /**
     * Converts value to corresponding enum.
     *
     * @param enumType enum type
     * @param value    database value
     * @param <E>      enum generic
     * @return corresponding enum
     */
    static <E extends IntValueEnum> E valueToEnum(Class<E> enumType, int value) {
        Assert.notNull(enumType, "enum type must not be null");
        Assert.notNull(value, "value must not be null");
        Assert.isTrue(enumType.isEnum(), "type must be an enum type");

        return Stream.of(enumType.getEnumConstants())
            .filter(item -> item.getValue() == value)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("unknown database value: " + value));
    }

    /**
     * Gets enum value.
     *
     * @return enum value
     */
    int getValue();

}

