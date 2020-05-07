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
 * @date 2020-05-02 19:05
 */
@Data
@Entity
@Table(name = "contest_register")
@ToString
@EqualsAndHashCode(callSuper = true)
public class ContestRegisterEntity extends BaseEntity {
    @Column
    Long contestId;

    @Column
    Long userId;

    @Column(length = 64)
    String actualName;

    @Column(length = 64)
    String college;

    @Column(length = 64)
    String discipline;

    @Column
    Boolean sex;

    @Column
    Boolean status;
}
