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
 * @date 2020-05-02 19:50
 */
@Data
@Entity
@Table(name = "solution_code")
@ToString
@EqualsAndHashCode(callSuper = true)
public class SolutionCodeEntity extends BaseEntity {
    @Lob
    @Column
    String code;
}
