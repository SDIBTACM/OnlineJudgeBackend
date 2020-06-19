package cn.edu.sdtbu.model.entity.problem;

import cn.edu.sdtbu.model.constant.OnlineJudgeConstant;
import cn.edu.sdtbu.model.entity.base.BaseEntityWithDeleteTs;
import cn.edu.sdtbu.model.enums.ProblemType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-14 16:41
 */
@Data
@Entity
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "problem", indexes = {
    @Index(columnList = "ownerId", name = "idx_owner_id"),
    @Index(columnList = "source", name = "idx_source"),
    @Index(columnList = "title", name = "idx_title")
})
public class ProblemEntity extends BaseEntityWithDeleteTs {
    @Column
    Long        ownerId;
    @Column(length = 128)
    String      title;
    @Column(length = 128)
    String      source;
    /**
     * millisecond
     */
    @Column
    Integer     timeLimit;
    /**
     * k bytes
     */
    @Column
    Integer     memoryLimit;
    @Column
    Boolean     specialJudge;
    @Column
    ProblemType problemType;
    @Column
    Long        similarFrom;
    @Column
    Boolean     hide;
    @Column
    Long submitCount;
    @Column
    Long acceptedCount;
    @Column
    Timestamp   testDataUpdatedAt;
    @PrePersist
    void preInsert() {
        if (submitCount == null) {
            submitCount = 0L;
        }
        if (acceptedCount == null) {
            acceptedCount = 0L;
        }
    }
    public static ProblemEntity getDefaultValue() {
        ProblemEntity entity = new ProblemEntity();
        entity.hide = false;
        entity.specialJudge = false;
        entity.problemType = ProblemType.NORMAL;
        entity.timeLimit = 1000;
        entity.memoryLimit = 1 << 10;
        entity.testDataUpdatedAt = OnlineJudgeConstant.TIME_ZERO;
        return entity;
    }
}

