package cn.edu.sdtbu.model.enums;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-03 21:26
 */
public enum KeyPrefix {
    ENTITY,USER_NAME,
    /******************    count     ********************/
    USER_ACCEPTED_COUNT,
    USER_SUBMIT_COUNT,
    TOTAL_SUBMIT_COUNT,
    PROBLEM_CONTEST_ACCEPTED,
    PROBLEM_CONTEST_SUBMIT_COUNT,
    BANED_USER,

    PROBLEM_TOTAL_ACCEPT,
    PROBLEM_TOTAL_SUBMIT,
    /******************     list     ********************/
    USERS_RANK_LIST_DTO, CONTEST_PRIVILEGE, JUDGE_RESULT;
}
