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
 * @date 2020-05-04 14:52
 */
@Data
@Entity
@Table(name = "mail")
@ToString
@EqualsAndHashCode(callSuper = true)
public class MailEntity extends BaseEntity {
    @Column
    Long fromUserId;

    @Column
    Long toUserId;

    @Column
    String topic;

    @Column
    Boolean status;
}
