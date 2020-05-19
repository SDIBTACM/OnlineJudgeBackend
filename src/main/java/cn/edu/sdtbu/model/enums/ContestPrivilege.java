package cn.edu.sdtbu.model.enums;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-27 09:29
 */
public enum  ContestPrivilege implements IntValueEnum {
    PUBLIC(0),
    NEED_REGISTER(1),
    PROTECT(2),
    PRIVATE(3);

    private final int value;
    ContestPrivilege(int value) {
        this.value = value;
    }
    @Override
    public int getValue() {
        return value;
    }
}
