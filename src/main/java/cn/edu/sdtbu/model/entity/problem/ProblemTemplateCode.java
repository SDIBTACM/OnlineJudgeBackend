package cn.edu.sdtbu.model.entity.problem;

import cn.edu.sdtbu.model.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-06 15:03
 */
@Data
@Entity
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name = "problem_template_code", indexes = {
    @Index(columnList = "problemId", name = "unique_problem_id", unique = true)
})
public class ProblemTemplateCode extends BaseEntity {

    @Column
    Long problemId;

    @Lob
    @Column
    String extraCode;

}
