package cn.edu.sdtbu.model.enums;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-19 17:53
 */
public enum ContestPrivilegeTypeEnum implements IntValueEnum {
    DENY_TAKE_PART_IN(-1),
    ALLOW_TAKE_PART_IN(0),
    ALLOW_MANAGE(1);


    private final int value;
    ContestPrivilegeTypeEnum(int value) {
        this.value = value;
    }
    @Override
    public int getValue() {
        return value;
    }
}
