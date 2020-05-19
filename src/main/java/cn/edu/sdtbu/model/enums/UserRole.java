package cn.edu.sdtbu.model.enums;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 15:44
 */
public enum UserRole implements IntValueEnum {
    // locked, can't login
    LOCKED(-1),
    // default role
    STUDENT(0),
    // teacher, need admin change role
    TEACHER(1),
    // admin
    ADMIN(2);

    private final int value;
    @Override
    public int getValue() {
        return value;
    }
    UserRole(int value) {
        this.value = value;
    }
}
