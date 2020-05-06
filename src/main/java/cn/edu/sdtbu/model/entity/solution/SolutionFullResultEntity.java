package cn.edu.sdtbu.model.entity.solution;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 15:10
 */
@Data
@Entity
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "solution_full_result", indexes = {
    @Index(columnList = "solution_id", name = "unique_solution_id", unique = true)
})
public class SolutionFullResultEntity extends BaseEntity {
    @Column(name = "solution_id")
    Long solutionId;

    @Lob
    @Column
    String data;
}
