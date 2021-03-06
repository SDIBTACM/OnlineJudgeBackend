package cn.edu.sdtbu.model.entity.solution;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
import cn.edu.sdtbu.model.enums.JudgeResult;
import cn.edu.sdtbu.model.enums.LangType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-21 14:52
 */
@Data
@Entity
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "solution", indexes = {
    @Index(name = "idx_owner_id", columnList = "ownerId"),
    @Index(name = "idx_problem_id", columnList = "problemId"),
    @Index(name = "idx_contest_id", columnList = "contestId"),


})
public class SolutionEntity extends BaseEntity {
    @Column
    Long        ownerId;
    @Column
    Long        problemId;
    @Column
    Long        contestId;
    @Column
    String      hash;
    @Column
    LangType    lang;
    @Column
    JudgeResult result;
    @Column
    Integer     timeUsed;
    @Column
    Integer     memoryUsed;
    @Column
    Long        similarAt;
    @Column
    Long        similarPercent;
    @Column
    Integer     codeLength;
}
