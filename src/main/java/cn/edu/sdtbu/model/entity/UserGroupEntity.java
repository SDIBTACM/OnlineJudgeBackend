package cn.edu.sdtbu.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Soul
 * @version 1.0
 * @date 2020-05-04 14:40
 */
@Data
@Entity
@Table(name = "user_group")
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserGroupEntity extends BaseEntity {
    @Column
    Long userId;

    @Column
    Long GroupId;
}
