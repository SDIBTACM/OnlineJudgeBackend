package cn.edu.sdtbu.model.enums;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-23 16:38
 */
public enum SecurityType implements IntValueEnum {
    NONE(0),
    STUDENT_OR_LOGIN(1),
    AT_LEAST_TEACHER(2),
    AT_LEAST_ADMIN(3);

    private final int value;

    SecurityType(int value) {
        this.value = value;
    }


    @Override
    public int getValue() {
        return value;
    }
}
