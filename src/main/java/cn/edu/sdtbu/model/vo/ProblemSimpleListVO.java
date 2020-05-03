package cn.edu.sdtbu.model.vo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-02 10:48
 */
@Data
public class ProblemSimpleListVO {
    Long problemId;

    String title;

    Long acCount;

    Long submitCount;

    Long submitPeopleCount;

    Boolean isAccept;

    Timestamp lastSubmit;
}
