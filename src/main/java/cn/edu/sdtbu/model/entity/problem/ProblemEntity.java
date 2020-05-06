package cn.edu.sdtbu.model.entity.problem;

import cn.edu.sdtbu.model.entity.base.BaseEntityWithDeleteTs;
import cn.edu.sdtbu.model.enums.ProblemType;
import cn.edu.sdtbu.model.properties.Const;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
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
    @Index(columnList = "owner_id", name = "idx_owner_id"),
    @Index(columnList = "source", name = "idx_source"),
    @Index(columnList = "title", name = "idx_title")
})
public class ProblemEntity extends BaseEntityWithDeleteTs {
    @Column(name = "owner_id")
    Long ownerId;
    @Column(length = 128)
    String title;
    @Column(length = 128)
    String source;
    /**
     * millisecond
     */
    @Column
    Integer timeLimit;
    /**
     * k bytes
     */
    @Column
    Integer memoryLimit;
    @Column
    Boolean specialJudge;
    @Column
    ProblemType problemType;
    @Column
    Long similarFrom;
    @Column
    Boolean hide;
    @Column
    Timestamp testDataUpdatedAt;
    public static ProblemEntity getDefaultValue() {
        ProblemEntity entity = new ProblemEntity();
        entity.timeLimit = 1000;
        entity.memoryLimit = 1 << 10;
        entity.testDataUpdatedAt = Const.TIME_ZERO;
        return entity;
    }
}

