package cn.edu.sdtbu.model.entity.contest;


import cn.edu.sdtbu.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-05-01 16:42
 */
@Data
@Entity
@Table(name = "contest_problem")
@ToString
@EqualsAndHashCode(callSuper = true)
public class ContestProblemEntity extends BaseEntity {
    @Column
    Long contestId;

    @Column
    Long problemId;

    @Column(length = 128)
    String title;

    @Column
    Integer problemOrder;

}
