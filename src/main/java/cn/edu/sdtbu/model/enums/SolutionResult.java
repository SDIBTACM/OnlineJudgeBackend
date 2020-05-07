package cn.edu.sdtbu.model.enums;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-21 15:35
 */
public enum SolutionResult {
    UNKNOWN,
    PENDING,
    PENDING_REJUDGE,
    COMPILING,
    RUNNING_AND_JUDGING,
    ACCEPT,
    WRONG_ANSWER,
    TIME_LIMIT_EXCEEDED,
    MEMORY_LIMIT_EXCEEDED,
    OUTPUT_LIMIT_EXCEEDED,
    RUNTIME_ERROR,
    COMPILE_ERROR;
}
