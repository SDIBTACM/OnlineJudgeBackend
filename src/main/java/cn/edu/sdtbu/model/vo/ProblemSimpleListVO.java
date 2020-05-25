package cn.edu.sdtbu.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-02 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemSimpleListVO {
    Long problemId;

    String title;

    Long acCount;

    Long submitCount;

    Long submitPeopleCount;

    Boolean isAccepted;

    Boolean hide;

    Timestamp lastSubmit;

}
