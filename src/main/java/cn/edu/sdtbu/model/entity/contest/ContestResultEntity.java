package cn.edu.sdtbu.model.entity.contest;

import cn.edu.sdtbu.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-05-01 17:03
 */
@Data
@Entity
@Table(name = "contest_result")
@ToString
@EqualsAndHashCode(callSuper = true)
public class ContestResultEntity extends BaseEntity {
    @Column
    Long contestProblemId;

    @Column
    Long userId;

    @Column
    Long contestId;

    @Column
    Long submitCount;

    @Column
    Timestamp acAt;
}
