package cn.edu.sdtbu.model.constant;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-03 21:26
 */
public interface KeyPrefixConstant {
    /**
     * 普通 Key-Value 使用的前缀
     */
    String ENTITY              = "Entity";
    String USERNAME            = "Username";
    String USER_ACCEPTED_COUNT = "UserAcceptedCount";
    String LOCKED_USER         = "LockedUser";
    String CONTEST_PRIVILEGE   = "ContestPrivilege";
    String JUDGE_RESULT        = "JudgeResult";
    String REGISTER_USER       = "RegisterUser";
    String REGISTERED_EMAIL    = "RegisteredMail";
    String REGISTERED_USERNAME = "RegisteredUserName";
    String CONTEST_PROBLEM     = "ContestProblem";
    String UNDEFINED           = "Undefined";
    String SUBMIT_CODE_MD5     = "submitCodeMD5";

    /**
     * 计数需要使用的前缀
     */
    String USER_SUBMIT_COUNT                = "UserSubmitCount";
    String TOTAL_SUBMIT_COUNT               = "TotalSubmitCount";
    String PROBLEM_CONTEST_ACCEPTED         = "ProblemContestAccepted";
    String PROBLEM_CONTEST_SUBMIT_COUNT     = "ProblemContestSubmitCount";
    String PROBLEM_TOTAL_SUBMIT_PEOPLE      = "ProblemTotalSubmitPeople";
    String PROBLEM_TOTAL_ACCEPT             = "ProblemTotalAccepted";
    String PROBLEM_TOTAL_SUBMIT             = "ProblemTotalSubmit";
    String ALL_CONTEST_PROBLEM_SUBMIT_COUNT = "AllContestProblemSubmitCount";

    /**
     * 列表
     */
    String USERS_RANK_LIST_DTO = "UsersRankListDTO";
}
