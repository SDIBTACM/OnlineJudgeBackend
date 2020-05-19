package cn.edu.sdtbu.model.enums;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-21 15:31
 */

public enum  LangType implements IntValueEnum {
    //format: LANG_VERSION
    /**
     * c11
     */
    C_11(0),
    /**
     * c98
     */
    C_98(1),
    /**
     * java 8
     */
    JAVA_8(2),
    /**
     * py 3
     */
    PYTHON_3(3),
    /**
     * py 2
     */
    PYTHON_2(4);

    private final int value;
    LangType(int value) {
        this.value = value;
    }
    @Override
    public int getValue() {
        return value;
    }
}
