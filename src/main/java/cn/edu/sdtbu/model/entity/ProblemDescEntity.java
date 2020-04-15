package cn.edu.sdtbu.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-15 09:16
 */

@Data
@Entity
@Table(name = "problem_descripton", indexes = {
    @Index(name = "unique_problem_id", columnList = "problem_id", unique = true)}
)
@ToString
@EqualsAndHashCode(callSuper = true)
public class ProblemDescEntity extends BaseEntity {
    @Column(name = "problem_id")
    private Long problemId;

    @Lob
    @Column
    private String description;

    @Lob
    @Column
    private String input;

    @Lob
    @Column
    private String output;

    @Lob
    @Column
    private String sample;

    @Lob
    @Column
    private String hint;
}
