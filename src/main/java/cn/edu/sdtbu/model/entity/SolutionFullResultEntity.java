package cn.edu.sdtbu.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-05-02 19:59
 */
@Data
@Entity
@Table(name = "solution_full_result")
@ToString
@EqualsAndHashCode(callSuper = true)
public class SolutionFullResultEntity extends BaseEntity {
    @Lob
    @Column
    String data;
}
