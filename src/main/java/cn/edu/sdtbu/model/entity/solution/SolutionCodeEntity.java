package cn.edu.sdtbu.model.entity.solution;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 15:06
 */
@Data
@Entity
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "solution_code", indexes = {
    @Index(columnList = "solutionId", name = "unique_solution_id", unique = true)
})
public class SolutionCodeEntity extends BaseEntity {
    @Column
    Long solutionId;

    @Lob
    @Column
    String code;
}
